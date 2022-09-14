package com.intexsoft.analytics.mapper;

import com.intexsoft.analytics.dto.employee.TitleSalaryForkDto;
import com.intexsoft.analytics.dto.authentication.AuthenticationDto;
import com.intexsoft.analytics.dto.department.DepartmentDto;
import com.intexsoft.analytics.dto.employee.EmployeeDto;
import com.intexsoft.analytics.dto.employee.UpsertEmployeeRequestDto;
import com.intexsoft.analytics.model.Authentication;
import com.intexsoft.analytics.model.Department;
import com.intexsoft.analytics.model.Employee;
import com.intexsoft.analytics.model.TitleSalaryFork;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnalyticsMapper {

    AuthenticationDto toAuthenticationDto(Authentication authentication);

    DepartmentDto toDepartmentDto(Department department);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    Employee toEmployee(UpsertEmployeeRequestDto requestDto);

    EmployeeDto toEmployeeDto(Employee employee);

    TitleSalaryForkDto toTitleSalaryForkDto(TitleSalaryFork titleSalaryFork);

}
