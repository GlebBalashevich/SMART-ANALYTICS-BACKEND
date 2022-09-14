package com.intexsoft.analytics.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.intexsoft.analytics.dto.employee.UpsertEmployeeRequestDto;
import com.intexsoft.analytics.model.Authentication;
import com.intexsoft.analytics.model.Department;
import com.intexsoft.analytics.model.Employee;
import com.intexsoft.analytics.model.Title;
import com.intexsoft.analytics.model.TitleSalaryFork;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({ MockitoExtension.class})
class AnalyticsMapperTests {

    private AnalyticsMapper analyticsMapper;

    @BeforeEach
    void init(){
        analyticsMapper = new AnalyticsMapperImpl();
    }

    @Test
    void testToAuthenticationDto(){
        var authentication = Authentication.builder()
                .email("example@mail.com")
                .departmentId(UUID.randomUUID())
                .build();

        var actual = analyticsMapper.toAuthenticationDto(authentication);

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getEmail()).isEqualTo(authentication.getEmail());
        Assertions.assertThat(actual.getDepartmentId()).isEqualTo(authentication.getDepartmentId());
    }

    @Test
    void testToDepartmentDto(){
        var department = Department.builder()
                .id(UUID.randomUUID())
                .name("Test Department")
                .salaryBudget(BigDecimal.valueOf(100))
                .build();

        var actual = analyticsMapper.toDepartmentDto(department);

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getId()).isEqualTo(department.getId());
        Assertions.assertThat(actual.getName()).isEqualTo(department.getName());
        Assertions.assertThat(actual.getSalaryBudget()).isEqualTo(department.getSalaryBudget());
    }

    @Test
    void testToEmployee(){
        var upsertEmployeeRequestDto = UpsertEmployeeRequestDto.builder()
                .departmentId(UUID.randomUUID())
                .email("example@mail.com")
                .givenName("John")
                .familyName("Doe")
                .birthDate(LocalDate.parse("1979-01-01"))
                .hireDate(LocalDate.parse("2022-01-01"))
                .salary(BigDecimal.valueOf(5000))
                .title(Title.LEAD)
                .build();

        var actual = analyticsMapper.toEmployee(upsertEmployeeRequestDto);

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getId()).isNull();
        Assertions.assertThat(actual.getDepartmentId()).isEqualTo(upsertEmployeeRequestDto.getDepartmentId());
        Assertions.assertThat(actual.getEmail()).isEqualTo(upsertEmployeeRequestDto.getEmail());
        Assertions.assertThat(actual.getGivenName()).isEqualTo(upsertEmployeeRequestDto.getGivenName());
        Assertions.assertThat(actual.getFamilyName()).isEqualTo(upsertEmployeeRequestDto.getFamilyName());
        Assertions.assertThat(actual.getBirthDate()).isEqualTo(upsertEmployeeRequestDto.getBirthDate());
        Assertions.assertThat(actual.getHireDate()).isEqualTo(upsertEmployeeRequestDto.getHireDate());
        Assertions.assertThat(actual.getSalary()).isEqualTo(upsertEmployeeRequestDto.getSalary());
        Assertions.assertThat(actual.getTitle()).isEqualTo(upsertEmployeeRequestDto.getTitle());
        Assertions.assertThat(actual.getIsDeleted()).isFalse();
        Assertions.assertThat(actual.getCreateDate()).isNull();
        Assertions.assertThat(actual.getLastUpdateDate()).isNull();
    }

    @Test
    void testToEmployeeDto(){
        var employee = Employee.builder()
                .id(UUID.randomUUID())
                .departmentId(UUID.randomUUID())
                .email("example@mail.com")
                .givenName("John")
                .familyName("Doe")
                .birthDate(LocalDate.parse("1979-01-01"))
                .hireDate(LocalDate.parse("2022-01-01"))
                .salary(BigDecimal.valueOf(5000))
                .title(Title.LEAD)
                .build();

        var actual = analyticsMapper.toEmployeeDto(employee);

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getId()).isEqualTo(employee.getId());
        Assertions.assertThat(actual.getDepartmentId()).isEqualTo(employee.getDepartmentId());
        Assertions.assertThat(actual.getEmail()).isEqualTo(employee.getEmail());
        Assertions.assertThat(actual.getGivenName()).isEqualTo(employee.getGivenName());
        Assertions.assertThat(actual.getFamilyName()).isEqualTo(employee.getFamilyName());
        Assertions.assertThat(actual.getBirthDate()).isEqualTo(employee.getBirthDate());
        Assertions.assertThat(actual.getHireDate()).isEqualTo(employee.getHireDate());
        Assertions.assertThat(actual.getSalary()).isEqualTo(employee.getSalary());
        Assertions.assertThat(actual.getTitle()).isEqualTo(employee.getTitle());
    }

    @Test
    void testToTitleSalaryForkDto(){
        var titleSalaryFork = TitleSalaryFork.builder()
                .title(Title.LEAD)
                .minSalary(BigDecimal.valueOf(4500))
                .maxSalary(BigDecimal.valueOf(5500))
                .build();

        var actual = analyticsMapper.toTitleSalaryForkDto(titleSalaryFork);

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getTitle()).isEqualTo(titleSalaryFork.getTitle());
        Assertions.assertThat(actual.getMinSalary()).isEqualTo(titleSalaryFork.getMinSalary());
        Assertions.assertThat(actual.getMaxSalary()).isEqualTo(titleSalaryFork.getMaxSalary());
    }

}
