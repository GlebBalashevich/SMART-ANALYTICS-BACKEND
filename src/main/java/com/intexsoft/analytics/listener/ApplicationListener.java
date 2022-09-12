package com.intexsoft.analytics.listener;

import java.math.BigDecimal;

import com.intexsoft.analytics.dto.department.DepartmentDto;
import com.intexsoft.analytics.service.DepartmentService;
import com.intexsoft.analytics.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Profile("!test")
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationListener {

    private final DepartmentService departmentService;

    private final EmployeeService employeeService;

    @EventListener(ApplicationReadyEvent.class)
    public Mono<Void> setupDepartmentSalaryBudget() {
        return departmentService.retrieveAllDepartments()
                .flatMap(this::setupDepartmentSalaryBudget)
                .then();
    }

    private Mono<DepartmentDto> setupDepartmentSalaryBudget(DepartmentDto departmentDto) {
        return employeeService.retrieveEmployeesByDepartmentId(departmentDto.getId())
                .collectList()
                .map(employeeDtos -> employeeDtos.stream()
                        .reduce(BigDecimal.ZERO, (x, employeeDto) -> x.add(employeeDto.getSalary()), BigDecimal::add))
                .doOnNext(salaryBudget -> log.debug("Salary budget for department:{} is {}", departmentDto.getId(),
                        salaryBudget))
                .flatMap(salaryBudget -> departmentService.setupSalaryBudget(departmentDto.getId(), salaryBudget));
    }

}
