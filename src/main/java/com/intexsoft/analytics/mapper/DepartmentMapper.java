package com.intexsoft.analytics.mapper;

import com.intexsoft.analytics.dto.department.DepartmentDto;
import com.intexsoft.analytics.model.Department;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    DepartmentDto toDepartmentDto(Department department);

}
