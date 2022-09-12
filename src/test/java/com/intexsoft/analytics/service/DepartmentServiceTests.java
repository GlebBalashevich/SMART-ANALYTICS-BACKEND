package com.intexsoft.analytics.service;

import java.math.BigDecimal;
import java.util.UUID;

import com.intexsoft.analytics.TestDataProvider;
import com.intexsoft.analytics.exception.DepartmentException;
import com.intexsoft.analytics.mapper.AnalyticsMapper;
import com.intexsoft.analytics.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith({ MockitoExtension.class })
class DepartmentServiceTests {

    private DepartmentService departmentService;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private AnalyticsMapper analyticsMapper;

    @BeforeEach
    void init() {
        departmentService = new DepartmentService(departmentRepository, analyticsMapper);
    }

    @Test
    void testRetrieveDepartmentById() {
        var id = UUID.randomUUID();
        var department = TestDataProvider.getDepartmentStub(id);
        var expected = TestDataProvider.getDepartmentDtoStub(id);

        when(departmentRepository.findById(id)).thenReturn(Mono.just(department));
        when(analyticsMapper.toDepartmentDto(department)).thenReturn(expected);

        StepVerifier.create(departmentService.retrieveDepartmentById(id))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void testRetrieveDepartmentByIdNotFoundException() {
        var id = UUID.randomUUID();
        when(departmentRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(departmentService.retrieveDepartmentById(id))
                .verifyError(DepartmentException.class);
    }

    @Test
    void testRetrieveAllDepartments() {
        var id = UUID.randomUUID();
        var department = TestDataProvider.getDepartmentStub(id);
        var expected = TestDataProvider.getDepartmentDtoStub(id);

        when(departmentRepository.findAll()).thenReturn(Flux.just(department));
        when(analyticsMapper.toDepartmentDto(department)).thenReturn(expected);

        StepVerifier.create(departmentService.retrieveAllDepartments())
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void testRetrieveAllDepartmentsNoDepartments() {
        when(departmentRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(departmentService.retrieveAllDepartments())
                .verifyComplete();
    }

    @Test
    void testSetupSalaryBudget() {
        var salaryBudget = BigDecimal.valueOf(10000);
        var id = UUID.randomUUID();
        var department = TestDataProvider.getDepartmentStub(id);
        var expected = TestDataProvider.getDepartmentDtoStub(id);

        when(departmentRepository.findById(id)).thenReturn(Mono.just(department));
        when(departmentRepository.save(department.toBuilder().salaryBudget(salaryBudget).build())).thenReturn(
                Mono.just(department));
        when(analyticsMapper.toDepartmentDto(department)).thenReturn(expected);

        StepVerifier.create(departmentService.setupSalaryBudget(id, salaryBudget))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void testSetupSalaryBudgetNotFoundException() {
        var salaryBudget = BigDecimal.valueOf(10000);
        var id = UUID.randomUUID();

        when(departmentRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(departmentService.setupSalaryBudget(id, salaryBudget))
                .verifyError(DepartmentException.class);
    }

    @Test
    void testIncreaseSalaryBudget() {
        var salaryToAdd = BigDecimal.valueOf(10000);
        var id = UUID.randomUUID();
        var department = TestDataProvider.getDepartmentStub(id);
        var oldSalaryBudget = department.getSalaryBudget();
        var expected = TestDataProvider.getDepartmentDtoStub(id);

        when(departmentRepository.findById(id)).thenReturn(Mono.just(department));
        when(departmentRepository.save(department.toBuilder().salaryBudget(oldSalaryBudget.add(salaryToAdd)).build()))
                .thenReturn(Mono.just(department));
        when(analyticsMapper.toDepartmentDto(department)).thenReturn(expected);

        StepVerifier.create(departmentService.increaseSalaryBudget(id, salaryToAdd))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void testIncreaseSalaryBudgetNotFoundException() {
        var salaryToAdd = BigDecimal.valueOf(10000);
        var id = UUID.randomUUID();

        when(departmentRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(departmentService.increaseSalaryBudget(id, salaryToAdd))
                .verifyError(DepartmentException.class);
    }

    @Test
    void testUpdateSalaryBudget() {
        var salaryToAdd = BigDecimal.valueOf(10000);
        var salaryToReduce = BigDecimal.valueOf(5000);
        var id = UUID.randomUUID();
        var department = TestDataProvider.getDepartmentStub(id);
        var oldSalaryBudget = department.getSalaryBudget();
        var expected = TestDataProvider.getDepartmentDtoStub(id);
        var updatedSalaryBudget = oldSalaryBudget.add(salaryToAdd).subtract(salaryToReduce);

        when(departmentRepository.findById(id)).thenReturn(Mono.just(department));
        when(departmentRepository.save(department.toBuilder().salaryBudget(updatedSalaryBudget).build()))
                .thenReturn(Mono.just(department));
        when(analyticsMapper.toDepartmentDto(department)).thenReturn(expected);

        StepVerifier.create(departmentService.updateSalaryBudget(id, salaryToAdd, salaryToReduce))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void testUpdateSalaryBudgetNotFoundException() {
        var salaryToAdd = BigDecimal.valueOf(10000);
        var salaryToReduce = BigDecimal.valueOf(5000);
        var id = UUID.randomUUID();

        when(departmentRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(departmentService.updateSalaryBudget(id, salaryToAdd, salaryToReduce))
                .verifyError(DepartmentException.class);
    }

    @Test
    void testDecreaseSalaryBudget() {
        var salaryToReduce = BigDecimal.valueOf(5000);
        var id = UUID.randomUUID();
        var department = TestDataProvider.getDepartmentStub(id);
        var oldSalaryBudget = department.getSalaryBudget();
        var expected = TestDataProvider.getDepartmentDtoStub(id);

        when(departmentRepository.findById(id)).thenReturn(Mono.just(department));
        when(departmentRepository.save(department.toBuilder().salaryBudget(oldSalaryBudget.subtract(salaryToReduce))
                .build())).thenReturn(Mono.just(department));
        when(analyticsMapper.toDepartmentDto(department)).thenReturn(expected);

        StepVerifier.create(departmentService.decreaseSalaryBudget(id, salaryToReduce))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void testDecreaseSalaryBudgetNotFoundException() {
        var salaryToReduce = BigDecimal.valueOf(5000);
        var id = UUID.randomUUID();

        when(departmentRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(departmentService.decreaseSalaryBudget(id, salaryToReduce))
                .verifyError(DepartmentException.class);
    }
}
