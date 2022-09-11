package com.intexsoft.analytics.repository;

import java.time.LocalDate;
import java.util.UUID;

import com.intexsoft.analytics.model.Employee;
import com.intexsoft.analytics.model.Title;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeRepository extends ReactiveCrudRepository<Employee, UUID> {

    Flux<Employee> findEmployeesByDepartmentIdAndIsDeletedFalse(UUID departmentId);

    Mono<Boolean> existsEmployeeByEmail(String email);

    Mono<Employee> findEmployeeByEmail(String email);

    Flux<Employee> findAllByIsDeletedFalse();

    Mono<Employee> findFirstByDepartmentIdAndTitleOrderBySalaryAsc(UUID departmentId, Title title);

    Mono<Employee> findFirstByDepartmentIdAndTitleOrderBySalaryDesc(UUID departmentId, Title title);

}
