package com.intexsoft.analytics.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.intexsoft.analytics.model.JobRole;
import com.intexsoft.analytics.model.Title;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    private UUID id;

    private UUID departmentId;

    private String email;

    private String givenName;

    private String familyName;

    private LocalDate birthDate;

    private LocalDate hireDate;

    private BigDecimal salary;

    private Title title;

    private JobRole jobRole;

}
