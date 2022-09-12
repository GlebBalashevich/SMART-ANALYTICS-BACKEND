package com.intexsoft.analytics.service;

import java.math.BigDecimal;
import java.util.UUID;

import com.intexsoft.analytics.dto.department.DepartmentDto;
import com.intexsoft.analytics.exception.DepartmentException;
import com.intexsoft.analytics.mapper.AnalyticsMapper;
import com.intexsoft.analytics.repository.DepartmentRepository;
import com.intexsoft.analytics.util.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentService {

    private static final String DEPARTMENT_NOT_FOUND_ERROR_MESSAGE = "Department with id:%s not found";

    private final DepartmentRepository departmentRepository;

    private final AnalyticsMapper analyticsMapper;

    public Mono<DepartmentDto> retrieveDepartmentById(UUID id) {
        return departmentRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> error(String.format(DEPARTMENT_NOT_FOUND_ERROR_MESSAGE, id),
                        HttpStatus.NOT_FOUND, ErrorCode.DEPARTMENT_NOT_FOUND)))
                .doOnNext(department -> log.debug("Department with id:{} was found", id))
                .map(analyticsMapper::toDepartmentDto);
    }

    public Flux<DepartmentDto> retrieveAllDepartments() {
        return departmentRepository.findAll()
                .map(analyticsMapper::toDepartmentDto);
    }

    public Mono<DepartmentDto> setupSalaryBudget(UUID id, BigDecimal salary) {
        return departmentRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> error(String.format(DEPARTMENT_NOT_FOUND_ERROR_MESSAGE, id),
                        HttpStatus.NOT_FOUND, ErrorCode.DEPARTMENT_NOT_FOUND)))
                .doOnNext(department -> department.setSalaryBudget(salary))
                .flatMap(departmentRepository::save)
                .doOnNext(department -> log.debug("Salary budget:{} has been set up for Department:{}", salary, id))
                .map(analyticsMapper::toDepartmentDto);
    }

    public Mono<DepartmentDto> increaseSalaryBudget(UUID id, BigDecimal salaryToAdd) {
        return departmentRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> error(String.format(DEPARTMENT_NOT_FOUND_ERROR_MESSAGE, id),
                        HttpStatus.NOT_FOUND, ErrorCode.DEPARTMENT_NOT_FOUND)))
                .zipWhen(department -> Mono.just(department.getSalaryBudget().add(salaryToAdd)))
                .doOnNext(tuple -> tuple.getT1().setSalaryBudget(tuple.getT2()))
                .map(Tuple2::getT1)
                .flatMap(departmentRepository::save)
                .doOnNext(
                        department -> log.debug("Salary budget was increased on:{} for Department:{}", salaryToAdd, id))
                .map(analyticsMapper::toDepartmentDto);
    }

    public Mono<DepartmentDto> updateSalaryBudget(UUID id, BigDecimal salaryToAdd, BigDecimal salaryToReduce) {
        return departmentRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> error(String.format(DEPARTMENT_NOT_FOUND_ERROR_MESSAGE, id),
                        HttpStatus.NOT_FOUND, ErrorCode.DEPARTMENT_NOT_FOUND)))
                .zipWhen(
                        department -> Mono.just(department.getSalaryBudget().add(salaryToAdd).subtract(salaryToReduce)))
                .doOnNext(tuple -> tuple.getT1().setSalaryBudget(tuple.getT2()))
                .map(Tuple2::getT1)
                .flatMap(departmentRepository::save)
                .doOnNext(department -> log.debug("Salary budget was updated from {} to {} for Department:{}",
                        salaryToReduce, salaryToAdd, id))
                .map(analyticsMapper::toDepartmentDto);
    }

    public Mono<DepartmentDto> decreaseSalaryBudget(UUID id, BigDecimal salaryToReduce) {
        return departmentRepository.findById(id)
                .switchIfEmpty(Mono.defer(() -> error(String.format(DEPARTMENT_NOT_FOUND_ERROR_MESSAGE, id),
                        HttpStatus.NOT_FOUND, ErrorCode.DEPARTMENT_NOT_FOUND)))
                .zipWhen(department -> Mono.just(department.getSalaryBudget().subtract(salaryToReduce)))
                .doOnNext(tuple -> tuple.getT1().setSalaryBudget(tuple.getT2()))
                .map(Tuple2::getT1)
                .flatMap(departmentRepository::save)
                .doOnNext(department -> log.debug("Salary budget was decreased on {} for Department:{}", salaryToReduce,
                        id))
                .map(analyticsMapper::toDepartmentDto);
    }

    private <T> Mono<T> error(String message, HttpStatus status, String errorCode) {
        log.error(message);
        return Mono.error(new DepartmentException(message, status, errorCode));
    }
}
