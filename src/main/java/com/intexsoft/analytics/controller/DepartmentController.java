package com.intexsoft.analytics.controller;

import java.util.UUID;

import com.intexsoft.analytics.dto.department.DepartmentDto;
import com.intexsoft.analytics.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/departments", produces = MediaType.APPLICATION_JSON_VALUE)
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("{id}")
    @PreAuthorize("@serviceAccess.canAccess(authentication)")
    public Mono<DepartmentDto> retrieveDepartmentById(@PathVariable UUID id) {
        return departmentService.findDepartmentById(id);
    }

    @GetMapping
    @PreAuthorize("@serviceAccess.canAccess(authentication)")
    public Flux<DepartmentDto> retrieveAllDepartments() {
        return departmentService.findAllDepartments();
    }

}
