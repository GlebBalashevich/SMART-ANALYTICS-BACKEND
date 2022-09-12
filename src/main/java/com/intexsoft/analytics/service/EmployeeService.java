package com.intexsoft.analytics.service;

import java.math.BigDecimal;
import java.util.UUID;

import com.intexsoft.analytics.dto.department.DepartmentDto;
import com.intexsoft.analytics.dto.employee.EmployeeDto;
import com.intexsoft.analytics.dto.employee.UpsertEmployeeRequestDto;
import com.intexsoft.analytics.exception.EmployeeException;
import com.intexsoft.analytics.mapper.AnalyticsMapper;
import com.intexsoft.analytics.model.Employee;
import com.intexsoft.analytics.model.SelectionCriteria;
import com.intexsoft.analytics.model.Title;
import com.intexsoft.analytics.repository.EmployeeRepository;
import com.intexsoft.analytics.util.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private static final String EMPLOYEE_EXISTS_BY_EMAIL_ERROR_MESSAGE = "Employee with email:%s already exists";

    private static final String EMPLOYEE_NOT_FOUND_BY_ID_ERROR_MESSAGE = "Employee with id:%s not found";

    private final EmployeeRepository employeeRepository;

    private final AnalyticsMapper analyticsMapper;

    private final DepartmentService departmentService;

    @EventListener(ApplicationReadyEvent.class)
    public Mono<Void> setupDepartmentSalaryBudget() {
        return departmentService.findAllDepartments()
                .flatMap(this::setupDepartmentSalaryBudget)
                .then();
    }

    @Transactional
    public Mono<EmployeeDto> addEmployee(UpsertEmployeeRequestDto requestDto) {
        return employeeRepository.existsEmployeeByEmail(requestDto.getEmail())
                .filter(Boolean.FALSE::equals)
                .switchIfEmpty(Mono.defer(() -> error(String.format(EMPLOYEE_EXISTS_BY_EMAIL_ERROR_MESSAGE,
                        requestDto.getEmail()), HttpStatus.BAD_REQUEST, ErrorCode.EMPLOYEE_EXISTS_BY_EMAIL)))
                .flatMap(exists -> departmentService.findDepartmentById(requestDto.getDepartmentId()))
                .flatMap(department -> departmentService.increaseSalaryBudget(department.getId(),
                        requestDto.getSalary()))
                .map(department -> analyticsMapper.toEmployee(requestDto))
                .flatMap(employeeRepository::save)
                .doOnNext(employee -> log.debug("Employee with email:{} was successfully created, id:{}",
                        employee.getEmail(), employee.getId()))
                .map(analyticsMapper::toEmployeeDto);
    }

    public Mono<EmployeeDto> retrieveEmployeeById(UUID id) {
        return employeeRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> error(String.format(EMPLOYEE_NOT_FOUND_BY_ID_ERROR_MESSAGE, id),
                        HttpStatus.NOT_FOUND, ErrorCode.EMPLOYEE_NOT_FOUND_BY_ID)))
                .doOnNext(employee -> log.debug("Employee with id:{} was successfully found", id))
                .map(analyticsMapper::toEmployeeDto);
    }

    public Flux<EmployeeDto> retrieveEmployeesByDepartmentId(UUID departmentId) {
        return employeeRepository.findEmployeesByDepartmentIdAndIsDeletedFalse(departmentId)
                .map(analyticsMapper::toEmployeeDto);
    }

    public Mono<EmployeeDto> findEmployeeWithBorderSalary(UUID departmentId, Title title,
            SelectionCriteria selectionCriteria) {
        final var employee = SelectionCriteria.MAX == selectionCriteria
                ? employeeRepository.findFirstByDepartmentIdAndTitleOrderBySalaryDesc(departmentId, title)
                : employeeRepository.findFirstByDepartmentIdAndTitleOrderBySalaryAsc(departmentId, title);
        return employee
                .doOnNext(emp -> log.debug("Employee with title:{} for criteria:{} for department was found:{}", title,
                        selectionCriteria, departmentId))
                .defaultIfEmpty(Employee.builder().build())
                .map(analyticsMapper::toEmployeeDto);
    }

    @Transactional
    public Mono<EmployeeDto> updateEmployee(UUID id, UpsertEmployeeRequestDto requestDto) {
        return departmentService.findDepartmentById(requestDto.getDepartmentId())
                .flatMap(department -> employeeRepository.findById(id))
                .switchIfEmpty(Mono.defer(() -> error(String.format(EMPLOYEE_NOT_FOUND_BY_ID_ERROR_MESSAGE, id),
                        HttpStatus.NOT_FOUND, ErrorCode.EMPLOYEE_NOT_FOUND_BY_ID)))
                .zipWith(employeeRepository.findEmployeeByEmailAndIsDeletedFalse(requestDto.getEmail()))
                .filter(tuple -> tuple.getT1().getId().equals(tuple.getT2().getId()))
                .switchIfEmpty(Mono.defer(() -> error(String.format(EMPLOYEE_EXISTS_BY_EMAIL_ERROR_MESSAGE,
                        requestDto.getEmail()), HttpStatus.BAD_REQUEST, ErrorCode.EMPLOYEE_EXISTS_BY_EMAIL)))
                .map(Tuple2::getT1)
                .zipWhen(employee -> updateDepartmentSalaryBudget(employee, requestDto))
                .map(tuple -> mergeEmployee(tuple.getT1(), requestDto))
                .flatMap(employeeRepository::save)
                .doOnNext(emp -> log.debug("Employee with id:{} was successfully updated", id))
                .map(analyticsMapper::toEmployeeDto);
    }

    @Transactional
    public Mono<Void> deleteEmployee(UUID id) {
        return employeeRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> error(String.format(EMPLOYEE_NOT_FOUND_BY_ID_ERROR_MESSAGE, id),
                        HttpStatus.NOT_FOUND, ErrorCode.EMPLOYEE_NOT_FOUND_BY_ID)))
                .zipWhen(employee -> departmentService.decreaseSalaryBudget(employee.getDepartmentId(),
                        employee.getSalary()))
                .doOnNext(tuple -> tuple.getT1().setIsDeleted(Boolean.TRUE))
                .map(Tuple2::getT1)
                .flatMap(employeeRepository::save)
                .doOnNext(employee -> log.debug("Employee with id:{} was successfully deleted", id))
                .then();
    }

    private Employee mergeEmployee(Employee employee, UpsertEmployeeRequestDto requestDto) {
        log.debug("Mering data employee:{}, updating data:{}", employee, requestDto);
        return employee.toBuilder()
                .departmentId(requestDto.getDepartmentId())
                .givenName(requestDto.getGivenName())
                .familyName(requestDto.getFamilyName())
                .birthDate(requestDto.getBirthDate())
                .hireDate(requestDto.getHireDate())
                .salary(requestDto.getSalary())
                .title(requestDto.getTitle())
                .isDeleted(Boolean.FALSE)
                .build();
    }

    private Mono<DepartmentDto> setupDepartmentSalaryBudget(DepartmentDto departmentDto) {
        return retrieveEmployeesByDepartmentId(departmentDto.getId())
                .collectList()
                .map(employeeDtos -> employeeDtos.stream()
                        .reduce(BigDecimal.ZERO, (x, employeeDto) -> x.add(employeeDto.getSalary()), BigDecimal::add))
                .doOnNext(salaryBudget -> log.debug("Salary budget for department:{} is {}", departmentDto.getId(),
                        salaryBudget))
                .flatMap(salaryBudget -> departmentService.setupSalaryBudget(departmentDto.getId(), salaryBudget));
    }

    private Mono<DepartmentDto> updateDepartmentSalaryBudget(Employee employee, UpsertEmployeeRequestDto requestDto) {
        if (Boolean.TRUE.equals(employee.getIsDeleted())) {
            log.debug("Employee with id:{} was deleted, restoring data", employee.getId());
            return departmentService.increaseSalaryBudget(requestDto.getDepartmentId(), requestDto.getSalary());
        }
        return employee.getDepartmentId().equals(requestDto.getDepartmentId())
                ? departmentService.updateSalaryBudget(employee.getDepartmentId(), requestDto.getSalary(),
                employee.getSalary())
                : departmentService.decreaseSalaryBudget(employee.getDepartmentId(), employee.getSalary())
                .flatMap(department -> departmentService.increaseSalaryBudget(requestDto.getDepartmentId(),
                        requestDto.getSalary()));
    }

    private <T> Mono<T> error(String message, HttpStatus status, String errorCode) {
        log.error(message);
        return Mono.error(new EmployeeException(message, status, errorCode));
    }

}
