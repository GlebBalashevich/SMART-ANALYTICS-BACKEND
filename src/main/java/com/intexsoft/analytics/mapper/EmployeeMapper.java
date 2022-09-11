package com.intexsoft.analytics.mapper;

import com.intexsoft.analytics.dto.EmployeeDto;
import com.intexsoft.analytics.dto.UpsertEmployeeRequestDto;
import com.intexsoft.analytics.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    Employee toEmployee(UpsertEmployeeRequestDto requestDto);

    EmployeeDto toEmployeeDto(Employee employee);

}
