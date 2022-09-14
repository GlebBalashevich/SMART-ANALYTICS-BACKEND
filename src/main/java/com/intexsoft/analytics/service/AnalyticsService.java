package com.intexsoft.analytics.service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.intexsoft.analytics.dto.analytics.SalaryAnalyticsDto;
import com.intexsoft.analytics.dto.analytics.SeniorityAnalyticsDto;
import com.intexsoft.analytics.dto.department.DepartmentDto;
import com.intexsoft.analytics.dto.employee.EmployeeDto;
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

    public Mono<SalaryAnalyticsDto> findDepartmentSalaryForks(UUID departmentId) {
        return employeeService.findTitlesSalaryForks(departmentId)
                .sort(Comparator.comparing(titleSalaryForkDto -> titleSalaryForkDto.getTitle().getSeniorityIndex()))
                .collectList()
                .map(titleSalaryForkDtos -> SalaryAnalyticsDto.builder().titleSalaryForks(titleSalaryForkDtos).build());
    }

    public Mono<SeniorityAnalyticsDto> defineDepartmentSeniority(UUID departmentId) {
        return departmentService.retrieveDepartmentById(departmentId)
                .zipWith(employeeService.retrieveEmployeesByDepartmentId(departmentId).collectList()
                        .map(this::calculateSeniority))
                .map(tuple -> buildSeniorityAnalyticsDto(tuple.getT1(), tuple.getT2()));
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
