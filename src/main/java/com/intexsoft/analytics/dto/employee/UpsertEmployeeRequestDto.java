package com.intexsoft.analytics.dto.employee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.intexsoft.analytics.model.Title;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpsertEmployeeRequestDto {

    @NotNull
    private UUID departmentId;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(max = 50)
    private String givenName;

    @NotBlank
    @Size(max = 50)
    private String familyName;

    @NotNull
    @Past
    private LocalDate birthDate;

    @NotNull
    private LocalDate hireDate;

    @NotNull
    @Min(100)
    @Max(100_000)
    private BigDecimal salary;

    @NotNull
    private Title title;

}
