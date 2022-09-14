package com.intexsoft.analytics.service;

import java.util.List;
import java.util.UUID;

import com.intexsoft.analytics.TestDataProvider;
import com.intexsoft.analytics.dto.analytics.SalaryAnalyticsDto;
import com.intexsoft.analytics.exception.DepartmentException;
import com.intexsoft.analytics.util.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith({ MockitoExtension.class })
class AnalyticsServiceTests {

    private AnalyticsService analyticsService;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private EmployeeService employeeService;

    @BeforeEach
    void init() {
        analyticsService = new AnalyticsService(departmentService, employeeService);
    }

    @Test
    void testFindDepartmentBorderSalary() {
        var departmentId = UUID.randomUUID();
        var titleSalaryForkDto = TestDataProvider.getTitleSalaryForkDtoStub();
        var expected = SalaryAnalyticsDto.builder().titleSalaryForks(List.of(titleSalaryForkDto)).build();

        when(employeeService.findTitlesSalaryForks(departmentId)).thenReturn(Flux.just(titleSalaryForkDto));

        StepVerifier.create(analyticsService.findDepartmentSalaryForks(departmentId))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void testDefineDepartmentSeniority() {
        var departmentId = UUID.randomUUID();
        var departmentDto = TestDataProvider.getDepartmentDtoStub(departmentId);
        var employeeDto = TestDataProvider.getEmployeeDtoStub(UUID.randomUUID(), departmentId);
        var expected = TestDataProvider.getSeniorityAnalyticsDtoStub();

        when(departmentService.retrieveDepartmentById(departmentId)).thenReturn(Mono.just(departmentDto));
        when(employeeService.retrieveEmployeesByDepartmentId(departmentId))
                .thenReturn(Flux.just(employeeDto));

        StepVerifier.create(analyticsService.defineDepartmentSeniority(departmentId))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void testDefineDepartmentSeniorityDepartmentNotFoundException() {
        var departmentId = UUID.randomUUID();
        var employeeDto = TestDataProvider.getEmployeeDtoStub(UUID.randomUUID(), departmentId);

        when(departmentService.retrieveDepartmentById(departmentId)).thenReturn(
                Mono.error(() -> new DepartmentException("Department not found", HttpStatus.NOT_FOUND,
                        ErrorCode.DEPARTMENT_NOT_FOUND)));
        when(employeeService.retrieveEmployeesByDepartmentId(departmentId))
                .thenReturn(Flux.just(employeeDto));

        StepVerifier.create(analyticsService.defineDepartmentSeniority(departmentId))
                .verifyError(DepartmentException.class);
    }

    @Test
    void testDefineDepartmentSeniorityEmployeesNotFound() {
        var departmentId = UUID.randomUUID();
        var departmentDto = TestDataProvider.getDepartmentDtoStub(departmentId);
        var expected = TestDataProvider.getSeniorityAnalyticsDtoStub();
        expected.setSeniorityIndex(0.0);

        when(departmentService.retrieveDepartmentById(departmentId)).thenReturn(Mono.just(departmentDto));
        when(employeeService.retrieveEmployeesByDepartmentId(departmentId))
                .thenReturn(Flux.empty());

        StepVerifier.create(analyticsService.defineDepartmentSeniority(departmentId))
                .expectNext(expected)
                .verifyComplete();
    }

}
