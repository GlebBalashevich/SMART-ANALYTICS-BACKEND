package com.intexsoft.analytics.service;

import java.math.BigDecimal;
import java.util.UUID;

import com.intexsoft.analytics.TestDataProvider;
import com.intexsoft.analytics.dto.employee.EmployeeDto;
import com.intexsoft.analytics.exception.DepartmentException;
import com.intexsoft.analytics.model.SelectionCriteria;
import com.intexsoft.analytics.model.Title;
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
        var title = Title.LEAD;
        var selectionCriteria = SelectionCriteria.MAX;
        var departmentDto = TestDataProvider.getDepartmentDtoStub(departmentId);
        var employeeDto = TestDataProvider.getEmployeeDtoStub(UUID.randomUUID(), departmentId);
        var expected = TestDataProvider.getSalaryAnalyticsDtoStub();

        when(departmentService.findDepartmentById(departmentId)).thenReturn(Mono.just(departmentDto));
        when(employeeService.findEmployeeWithBorderSalary(departmentId, title, selectionCriteria))
                .thenReturn(Mono.just(employeeDto));

        StepVerifier.create(analyticsService.findDepartmentBorderSalary(departmentId, title, selectionCriteria))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void testFindDepartmentBorderSalaryDepartmentNotFoundException() {
        var departmentId = UUID.randomUUID();
        var title = Title.LEAD;
        var selectionCriteria = SelectionCriteria.MAX;
        var employeeDto = TestDataProvider.getEmployeeDtoStub(UUID.randomUUID(), departmentId);

        when(departmentService.findDepartmentById(departmentId)).thenReturn(
                Mono.error(() -> new DepartmentException("Department not found", HttpStatus.NOT_FOUND,
                        ErrorCode.DEPARTMENT_NOT_FOUND)));
        when(employeeService.findEmployeeWithBorderSalary(departmentId, title, selectionCriteria))
                .thenReturn(Mono.just(employeeDto));

        StepVerifier.create(analyticsService.findDepartmentBorderSalary(departmentId, title, selectionCriteria))
                .verifyError(DepartmentException.class);
    }

    @Test
    void testFindDepartmentBorderSalaryEmployeeNotFound() {
        var departmentId = UUID.randomUUID();
        var title = Title.LEAD;
        var selectionCriteria = SelectionCriteria.MAX;
        var departmentDto = TestDataProvider.getDepartmentDtoStub(departmentId);
        var expected = TestDataProvider.getSalaryAnalyticsDtoStub();
        expected.setSalaryValue(BigDecimal.ZERO);

        when(departmentService.findDepartmentById(departmentId)).thenReturn(Mono.just(departmentDto));
        when(employeeService.findEmployeeWithBorderSalary(departmentId, title, selectionCriteria))
                .thenReturn(Mono.just(EmployeeDto.builder().build()));

        StepVerifier.create(analyticsService.findDepartmentBorderSalary(departmentId, title, selectionCriteria))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void testDefineDepartmentSeniority() {
        var departmentId = UUID.randomUUID();
        var departmentDto = TestDataProvider.getDepartmentDtoStub(departmentId);
        var employeeDto = TestDataProvider.getEmployeeDtoStub(UUID.randomUUID(), departmentId);
        var expected = TestDataProvider.getSeniorityAnalyticsDtoStub();

        when(departmentService.findDepartmentById(departmentId)).thenReturn(Mono.just(departmentDto));
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

        when(departmentService.findDepartmentById(departmentId)).thenReturn(
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

        when(departmentService.findDepartmentById(departmentId)).thenReturn(Mono.just(departmentDto));
        when(employeeService.retrieveEmployeesByDepartmentId(departmentId))
                .thenReturn(Flux.empty());

        StepVerifier.create(analyticsService.defineDepartmentSeniority(departmentId))
                .expectNext(expected)
                .verifyComplete();
    }

}
