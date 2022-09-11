package com.intexsoft.analytics.dto.analytics;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaryAnalyticsDto {

    private String departmentName;

    private BigDecimal salaryValue;

}
