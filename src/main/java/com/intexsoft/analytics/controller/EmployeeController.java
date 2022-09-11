package com.intexsoft.analytics.controller;

import java.util.UUID;

import com.intexsoft.analytics.dto.EmployeeDto;
import com.intexsoft.analytics.dto.UpsertEmployeeRequestDto;
import com.intexsoft.analytics.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EmployeeDto> addEmployee(@RequestBody @Validated UpsertEmployeeRequestDto requestDto) {
        return employeeService.addEmployee(requestDto);
    }

    @GetMapping("{id}")
    public Mono<EmployeeDto> retrieveEmployeeById(@PathVariable UUID id) {
        return employeeService.retrieveEmployeeById(id);
    }

    @GetMapping
    public Flux<EmployeeDto> retrieveAllEmployees() {
        return employeeService.retrieveAllEmployees();
    }

    @GetMapping("/departments/{departmentId}")
    public Flux<EmployeeDto> retrieveEmployeesByDepartmentId(@PathVariable UUID departmentId) {
        return employeeService.retrieveEmployeesByDepartmentId(departmentId);
    }

    @PutMapping("{id}")
    public Mono<EmployeeDto> updateEmployee(@PathVariable UUID id,
            @RequestBody @Validated UpsertEmployeeRequestDto requestDto) {
        return employeeService.updateEmployee(id, requestDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteEmployee(@PathVariable UUID id) {
        return employeeService.deleteEmployee(id);
    }

}
