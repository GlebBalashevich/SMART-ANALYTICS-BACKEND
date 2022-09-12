package com.intexsoft.analytics.controller;

import java.util.UUID;

import com.intexsoft.analytics.dto.employee.EmployeeDto;
import com.intexsoft.analytics.dto.employee.UpsertEmployeeRequestDto;
import com.intexsoft.analytics.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/employees", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Employee API", description = "Api for operations with Employees")
@SecurityRequirement(name = "Bearer Authentication")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Add new Employee to the system. Authz - same department manager")
    @PreAuthorize("@serviceAccess.canAccessToDepartmentData(authentication, #requestDto.departmentId)")
    public Mono<EmployeeDto> addEmployee(@RequestBody @Validated UpsertEmployeeRequestDto requestDto) {
        log.debug("Requested Adding new employee with email:{}", requestDto.getEmail());
        return employeeService.addEmployee(requestDto);
    }

    @GetMapping("{id}")
    @Operation(description = "Retrieve Employee by Id")
    @PreAuthorize("@serviceAccess.canAccess(authentication)")
    public Mono<EmployeeDto> retrieveEmployeeById(@PathVariable UUID id) {
        log.debug("Requested Retrieving employee by id:{}", id);
        return employeeService.retrieveEmployeeById(id);
    }

    @GetMapping("/departments/{departmentId}")
    @Operation(description = "Retrieve Employees related to the specific department. Authz - same department manager")
    @PreAuthorize("@serviceAccess.canAccessToDepartmentData(authentication, #departmentId)")
    public Flux<EmployeeDto> retrieveEmployeesByDepartmentId(@PathVariable UUID departmentId) {
        log.debug("Requested Retrieving employees by departmentId:{}", departmentId);
        return employeeService.retrieveEmployeesByDepartmentId(departmentId);
    }

    @PutMapping("{id}")
    @Operation(description = "Update Employee info. Authz - same department manager")
    @PreAuthorize("@serviceAccess.canAccessToDepartmentData(authentication, #requestDto.departmentId)")
    public Mono<EmployeeDto> updateEmployee(@PathVariable UUID id,
            @RequestBody @Validated UpsertEmployeeRequestDto requestDto) {
        log.debug("Requested Updating employee with email:{}", requestDto.getEmail());
        return employeeService.updateEmployee(id, requestDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(description = "Delete Employee info. Authz - same department manager")
    @PreAuthorize("@serviceAccess.canAccess(authentication)")
    public Mono<Void> deleteEmployee(@PathVariable UUID id) {
        log.debug("Requested Deleting employee by id:{}", id);
        return employeeService.deleteEmployee(id);
    }

}
