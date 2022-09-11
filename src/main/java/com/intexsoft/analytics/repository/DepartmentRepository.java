package com.intexsoft.analytics.repository;

import java.util.UUID;

import com.intexsoft.analytics.model.Department;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface DepartmentRepository extends ReactiveCrudRepository<Department, UUID> {

    Mono<Boolean> existsDepartmentByNameIgnoreCase(String name);

    Mono<Department> findDepartmentByNameIgnoreCase(String name);

}
