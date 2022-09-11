package com.intexsoft.analytics;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.intexsoft.analytics.dto.analytics.SalaryAnalyticsDto;
import com.intexsoft.analytics.dto.analytics.SeniorityAnalyticsDto;
import com.intexsoft.analytics.dto.department.DepartmentDto;
import com.intexsoft.analytics.dto.employee.EmployeeDto;
import com.intexsoft.analytics.model.Title;

public class TestDataProvider {

    public static DepartmentDto getDepartmentDtoStub(UUID id){
        return DepartmentDto.builder()
                .id(id)
                .name("Test Department")
                .salaryBudget(BigDecimal.valueOf(10000))
                .build();
    }

    public static EmployeeDto getEmployeeDtoStub(UUID id, UUID departmentId){
        return EmployeeDto.builder()
                .id(id)
                .departmentId(departmentId)
                .email("example@mail.com")
                .givenName("John")
                .familyName("Doe")
                .birthDate(LocalDate.parse("1979-01-01"))
                .hireDate(LocalDate.parse("2022-01-01"))
                .salary(BigDecimal.valueOf(5000))
                .title(Title.LEAD)
                .build();
    }

    public static SalaryAnalyticsDto getSalaryAnalyticsDtoStub(){
        return SalaryAnalyticsDto.builder()
                .departmentName("Test Department")
                .salaryValue(BigDecimal.valueOf(5000))
                .build();
    }

    public static SeniorityAnalyticsDto getSeniorityAnalyticsDtoStub(){
        return SeniorityAnalyticsDto.builder()
                .departmentName("Test Department")
                .seniorityIndex(4.0)
                .build();
    }

    private TestDataProvider(){}

}
