package com.intexsoft.analytics.service;

import java.util.UUID;

import com.intexsoft.analytics.TestDataProvider;
import com.intexsoft.analytics.dto.employee.EmployeeDto;
import com.intexsoft.analytics.exception.DepartmentException;
import com.intexsoft.analytics.exception.EmployeeException;
import com.intexsoft.analytics.mapper.AnalyticsMapper;
import com.intexsoft.analytics.model.Employee;
import com.intexsoft.analytics.model.SelectionCriteria;
import com.intexsoft.analytics.model.Title;
import com.intexsoft.analytics.repository.EmployeeRepository;
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
class EmployeeServiceTests {

    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AnalyticsMapper analyticsMapper;

    @Mock
    private DepartmentService departmentService;

    @BeforeEach
    void init() {
        employeeService = new EmployeeService(employeeRepository, analyticsMapper, departmentService);
    }

    @Test
    void testAddEmployee() {
        var id = UUID.randomUUID();
        var departmentId = UUID.randomUUID();
        var upsertEmployeeRequestDto = TestDataProvider.getUpsertEmployeeRequestDtoStub(departmentId);
        var departmentDto = TestDataProvider.getDepartmentDtoStub(departmentId);
        var employee = TestDataProvider.getEmployeeStub(id, departmentId);
        var expected = TestDataProvider.getEmployeeDtoStub(id, departmentId);

        when(employeeRepository.existsEmployeeByEmail(upsertEmployeeRequestDto.getEmail())).thenReturn(
                Mono.just(Boolean.FALSE));
        when(departmentService.retrieveDepartmentById(departmentId)).thenReturn(Mono.just(departmentDto));
        when(departmentService.increaseSalaryBudget(departmentId, upsertEmployeeRequestDto.getSalary())).thenReturn(
                Mono.just(departmentDto));
        when(analyticsMapper.toEmployee(upsertEmployeeRequestDto)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(Mono.just(employee));
        when(analyticsMapper.toEmployeeDto(employee)).thenReturn(expected);

        StepVerifier.create(employeeService.addEmployee(upsertEmployeeRequestDto))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void testAddEmployeeAlreadyExistsException() {
        var departmentId = UUID.randomUUID();
        var upsertEmployeeRequestDto = TestDataProvider.getUpsertEmployeeRequestDtoStub(departmentId);

        when(employeeRepository.existsEmployeeByEmail(upsertEmployeeRequestDto.getEmail())).thenReturn(
                Mono.just(Boolean.TRUE));

        StepVerifier.create(employeeService.addEmployee(upsertEmployeeRequestDto))
                .verifyError(EmployeeException.class);
    }

    @Test
    void testAddEmployeeDepartmentNotfoundException() {
        var departmentId = UUID.randomUUID();
        var upsertEmployeeRequestDto = TestDataProvider.getUpsertEmployeeRequestDtoStub(departmentId);

        when(employeeRepository.existsEmployeeByEmail(upsertEmployeeRequestDto.getEmail())).thenReturn(
                Mono.just(Boolean.FALSE));
        when(departmentService.retrieveDepartmentById(departmentId)).thenReturn(
                Mono.error(() -> new DepartmentException("Department not found", HttpStatus.NOT_FOUND,
                        ErrorCode.DEPARTMENT_NOT_FOUND)));

        StepVerifier.create(employeeService.addEmployee(upsertEmployeeRequestDto))
                .verifyError(DepartmentException.class);
    }

    @Test
    void testRetrieveEmployeeById() {
        var id = UUID.randomUUID();
        var departmentId = UUID.randomUUID();
        var employee = TestDataProvider.getEmployeeStub(id, departmentId);
        var expected = TestDataProvider.getEmployeeDtoStub(id, departmentId);

        when(employeeRepository.findById(id)).thenReturn(Mono.just(employee));
        when(analyticsMapper.toEmployeeDto(employee)).thenReturn(expected);

        StepVerifier.create(employeeService.retrieveEmployeeById(id))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void testRetrieveEmployeeByIdNotFoundException() {
        var id = UUID.randomUUID();

        when(employeeRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(employeeService.retrieveEmployeeById(id))
                .verifyError(EmployeeException.class);
    }

    @Test
    void testRetrieveEmployeesByDepartmentId() {
        var id = UUID.randomUUID();
        var departmentId = UUID.randomUUID();
        var employee = TestDataProvider.getEmployeeStub(id, departmentId);
        var expected = TestDataProvider.getEmployeeDtoStub(id, departmentId);

        when(employeeRepository.findEmployeesByDepartmentIdAndIsDeletedFalse(departmentId))
                .thenReturn(Flux.just(employee));
        when(analyticsMapper.toEmployeeDto(employee)).thenReturn(expected);

        StepVerifier.create(employeeService.retrieveEmployeesByDepartmentId(departmentId))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void testRetrieveEmployeesByDepartmentIdNoEmployees() {
        var departmentId = UUID.randomUUID();

        when(employeeRepository.findEmployeesByDepartmentIdAndIsDeletedFalse(departmentId))
                .thenReturn(Flux.empty());

        StepVerifier.create(employeeService.retrieveEmployeesByDepartmentId(departmentId))
                .verifyComplete();
    }

    @Test
    void testFindEmployeeWithMAXBorderSalary() {
        var title = Title.LEAD;
        var selectionCriteria = SelectionCriteria.MAX;
        var id = UUID.randomUUID();
        var departmentId = UUID.randomUUID();
        var employee = TestDataProvider.getEmployeeStub(id, departmentId);
        var expected = TestDataProvider.getEmployeeDtoStub(id, departmentId);

        when(employeeRepository.findFirstByDepartmentIdAndTitleOrderBySalaryDesc(departmentId, title))
                .thenReturn(Mono.just(employee));
        when(analyticsMapper.toEmployeeDto(employee)).thenReturn(expected);

        StepVerifier.create(employeeService.findEmployeeWithBorderSalary(departmentId, title, selectionCriteria))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void testFindEmployeeWithMINBorderSalary() {
        var title = Title.LEAD;
        var selectionCriteria = SelectionCriteria.MIN;
        var id = UUID.randomUUID();
        var departmentId = UUID.randomUUID();
        var employee = TestDataProvider.getEmployeeStub(id, departmentId);
        var expected = TestDataProvider.getEmployeeDtoStub(id, departmentId);

        when(employeeRepository.findFirstByDepartmentIdAndTitleOrderBySalaryAsc(departmentId, title))
                .thenReturn(Mono.just(employee));
        when(analyticsMapper.toEmployeeDto(employee)).thenReturn(expected);

        StepVerifier.create(employeeService.findEmployeeWithBorderSalary(departmentId, title, selectionCriteria))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void testFindEmployeeWithBorderSalaryNoEmployee() {
        var title = Title.LEAD;
        var selectionCriteria = SelectionCriteria.MIN;
        var departmentId = UUID.randomUUID();
        var expected = EmployeeDto.builder().build();

        when(employeeRepository.findFirstByDepartmentIdAndTitleOrderBySalaryAsc(departmentId, title))
                .thenReturn(Mono.empty());
        when(analyticsMapper.toEmployeeDto(Employee.builder().build())).thenReturn(expected);

        StepVerifier.create(employeeService.findEmployeeWithBorderSalary(departmentId, title, selectionCriteria))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void testUpdateEmployee() {
        var id = UUID.randomUUID();
        var departmentId = UUID.randomUUID();
        var upsertEmployeeRequestDto = TestDataProvider.getUpsertEmployeeRequestDtoStub(departmentId);
        var departmentDto = TestDataProvider.getDepartmentDtoStub(departmentId);
        var employee = TestDataProvider.getEmployeeStub(id, departmentId);
        var expected = TestDataProvider.getEmployeeDtoStub(id, departmentId);

        when(departmentService.retrieveDepartmentById(departmentId)).thenReturn(Mono.just(departmentDto));
        when(employeeRepository.findById(id)).thenReturn(Mono.just(employee));
        when(employeeRepository.findEmployeeByEmailAndIsDeletedFalse(upsertEmployeeRequestDto.getEmail()))
                .thenReturn(Mono.just(employee));
        when(departmentService.updateSalaryBudget(departmentId, employee.getSalary(),
                upsertEmployeeRequestDto.getSalary()))
                .thenReturn(Mono.just(departmentDto));
        when(employeeRepository.save(employee)).thenReturn(Mono.just(employee));
        when(analyticsMapper.toEmployeeDto(employee)).thenReturn(expected);

        StepVerifier.create(employeeService.updateEmployee(id, upsertEmployeeRequestDto))
                .expectNext(expected)
                .verifyComplete();
    }

    @Test
    void testUpdateEmployeeNotFoundException() {
        var id = UUID.randomUUID();
        var departmentId = UUID.randomUUID();
        var upsertEmployeeRequestDto = TestDataProvider.getUpsertEmployeeRequestDtoStub(departmentId);
        var departmentDto = TestDataProvider.getDepartmentDtoStub(departmentId);

        when(departmentService.retrieveDepartmentById(departmentId)).thenReturn(Mono.just(departmentDto));
        when(employeeRepository.findById(id)).thenReturn(Mono.empty());
        when(employeeRepository.findEmployeeByEmailAndIsDeletedFalse(upsertEmployeeRequestDto.getEmail()))
                .thenReturn(Mono.empty());

        StepVerifier.create(employeeService.updateEmployee(id, upsertEmployeeRequestDto))
                .verifyError(EmployeeException.class);
    }

    @Test
    void testUpdateEmployeeAnotherEmployeeEmailException() {
        var id = UUID.randomUUID();
        var departmentId = UUID.randomUUID();
        var upsertEmployeeRequestDto = TestDataProvider.getUpsertEmployeeRequestDtoStub(departmentId);
        upsertEmployeeRequestDto.setEmail("duplicate@mail.com");
        var departmentDto = TestDataProvider.getDepartmentDtoStub(departmentId);
        var employee = TestDataProvider.getEmployeeStub(id, departmentId);
        var employeeWithSameEmail = TestDataProvider.getEmployeeStub(UUID.randomUUID(), departmentId);
        employeeWithSameEmail.setEmail("duplicate@mail.com");

        when(departmentService.retrieveDepartmentById(departmentId)).thenReturn(Mono.just(departmentDto));
        when(employeeRepository.findById(id)).thenReturn(Mono.just(employee));
        when(employeeRepository.findEmployeeByEmailAndIsDeletedFalse(upsertEmployeeRequestDto.getEmail()))
                .thenReturn(Mono.just(employeeWithSameEmail));

        StepVerifier.create(employeeService.updateEmployee(id, upsertEmployeeRequestDto))
                .verifyError(EmployeeException.class);
    }

    @Test
    void testDeleteEmployee() {
        var id = UUID.randomUUID();
        var departmentId = UUID.randomUUID();
        var departmentDto = TestDataProvider.getDepartmentDtoStub(departmentId);
        var employee = TestDataProvider.getEmployeeStub(id, departmentId);

        when(employeeRepository.findById(id)).thenReturn(Mono.just(employee));
        when(departmentService.decreaseSalaryBudget(departmentId, employee.getSalary())).thenReturn(
                Mono.just(departmentDto));
        when(employeeRepository.save(employee.toBuilder().isDeleted(Boolean.TRUE).build())).thenReturn(
                Mono.just(employee));

        StepVerifier.create(employeeService.deleteEmployee(id))
                .verifyComplete();
    }

    @Test
    void testDeleteEmployeeNotFoundException() {
        var id = UUID.randomUUID();

        when(employeeRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(employeeService.deleteEmployee(id))
                .verifyError(EmployeeException.class);
    }
}
