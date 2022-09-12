package com.intexsoft.analytics.controller;

import java.util.UUID;

import com.intexsoft.analytics.dto.department.DepartmentDto;
import com.intexsoft.analytics.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Department API", description = "Api for operations with Department")
@SecurityRequirement(name = "Bearer Authentication")
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("{id}")
    @Operation(description = "Retrieve specific Department by Id")
    @PreAuthorize("@serviceAccess.canAccess(authentication)")
    public Mono<DepartmentDto> retrieveDepartmentById(@PathVariable UUID id) {
        log.debug("Requested Retrieving department by id:{}", id);
        return departmentService.findDepartmentById(id);
    }

    @GetMapping
    @Operation(description = "Retrieve all departments")
    @PreAuthorize("@serviceAccess.canAccess(authentication)")
    public Flux<DepartmentDto> retrieveAllDepartments() {
        log.debug("Requested Retrieving all departments");
        return departmentService.findAllDepartments();
    }

}
