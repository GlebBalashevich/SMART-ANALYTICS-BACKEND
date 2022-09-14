package com.intexsoft.analytics.repository;

import java.util.UUID;

import com.intexsoft.analytics.model.Employee;
import com.intexsoft.analytics.model.TitleSalaryFork;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeRepository extends ReactiveCrudRepository<Employee, UUID> {

    Flux<Employee> findEmployeesByDepartmentIdAndIsDeletedFalse(UUID departmentId);

    Mono<Boolean> existsEmployeeByEmail(String email);

    Mono<Employee> findEmployeeByEmailAndIsDeletedFalse(String email);

    @Query("SELECT employee.title, max(employee.salary) AS max_salary, min(employee.salary) AS min_salary " +
            "FROM employee WHERE employee.department_id = :departmentId GROUP BY title")
    Flux<TitleSalaryFork> findTitlesSalaryForksByDepartmentId(UUID departmentId);

}
