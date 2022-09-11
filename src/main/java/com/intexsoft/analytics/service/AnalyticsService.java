package com.intexsoft.analytics.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.intexsoft.analytics.dto.department.DepartmentDto;
import com.intexsoft.analytics.dto.employee.EmployeeDto;
import com.intexsoft.analytics.dto.analytics.SalaryAnalyticsDto;
import com.intexsoft.analytics.dto.analytics.SeniorityAnalyticsDto;
import com.intexsoft.analytics.model.SelectionCriteria;
import com.intexsoft.analytics.model.Title;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final DepartmentService departmentService;

    private final EmployeeService employeeService;

    public Mono<SalaryAnalyticsDto> findDepartmentBorderSalary(UUID departmentId, Title title,
            SelectionCriteria selectionCriteria) {
        return departmentService.findDepartmentById(departmentId)
                .zipWith(employeeService.findEmployeeWithBorderSalary(departmentId, title, selectionCriteria))
                .map(tuple -> buildSalaryAnalyticsDto(tuple.getT1(), tuple.getT2(), title, selectionCriteria));
    }

    public Mono<SeniorityAnalyticsDto> defineDepartmentSeniority(UUID departmentId) {
        return departmentService.findDepartmentById(departmentId)
                .zipWith(employeeService.retrieveEmployeesByDepartmentId(departmentId).collectList()
                        .map(this::calculateSeniority))
                .map(tuple -> buildSeniorityAnalyticsDto(tuple.getT1(), tuple.getT2()));
    }

    private SalaryAnalyticsDto buildSalaryAnalyticsDto(DepartmentDto departmentDto, EmployeeDto employeeDto,
            Title title, SelectionCriteria selectionCriteria) {
        final var salaryValue = employeeDto.getSalary() == null ? BigDecimal.ZERO : employeeDto.getSalary();
        return SalaryAnalyticsDto.builder()
                .departmentName(departmentDto.getName())
                .title(title)
                .selectionCriteria(selectionCriteria)
                .salaryValue(salaryValue)
                .build();
    }

    private Double calculateSeniority(List<EmployeeDto> employeeDtos) {
        if (employeeDtos.isEmpty()) {
            return 0.0;
        }
        final double employeesNumber = employeeDtos.size();
        final double senioritySum = employeeDtos.stream()
                .reduce(0, (x, employee) -> x + employee.getTitle().getSeniorityIndex(), Integer::sum);
        return senioritySum / employeesNumber;
    }

    private SeniorityAnalyticsDto buildSeniorityAnalyticsDto(DepartmentDto departmentDto, Double seniorityIndex) {
        return SeniorityAnalyticsDto.builder()
                .departmentName(departmentDto.getName())
                .seniorityIndex(seniorityIndex)
                .build();
    }

}
