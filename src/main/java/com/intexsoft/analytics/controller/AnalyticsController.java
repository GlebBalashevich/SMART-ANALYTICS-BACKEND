package com.intexsoft.analytics.controller;

import java.util.UUID;

import com.intexsoft.analytics.dto.analytics.SalaryAnalyticsDto;
import com.intexsoft.analytics.dto.analytics.SeniorityAnalyticsDto;
import com.intexsoft.analytics.model.SelectionCriteria;
import com.intexsoft.analytics.model.Title;
import com.intexsoft.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/analytics", produces = MediaType.APPLICATION_JSON_VALUE)
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/departments/{id}/salary")
    @PreAuthorize("@serviceAccess.canAccessToDepartmentData(authentication, #id)")
    public Mono<SalaryAnalyticsDto> findDepartmentBorderSalary(@PathVariable UUID id,
            @RequestParam Title title, @RequestParam SelectionCriteria selectionCriteria) {
        return analyticsService.findDepartmentBorderSalary(id, title, selectionCriteria);
    }

    @GetMapping("/departments/{id}/seniority")
    @PreAuthorize("@serviceAccess.canAccessToDepartmentData(authentication, #id)")
    public Mono<SeniorityAnalyticsDto> defineDepartmentSeniority(@PathVariable UUID id) {
        return analyticsService.defineDepartmentSeniority(id);
    }

}
