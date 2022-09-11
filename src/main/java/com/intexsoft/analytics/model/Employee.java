package com.intexsoft.analytics.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
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

    private Boolean isDeleted;

    @CreatedDate
    private Instant createDate;

    @LastModifiedDate
    private Instant lastUpdateDate;
}
