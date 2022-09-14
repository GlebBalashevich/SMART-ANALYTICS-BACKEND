package com.intexsoft.analytics.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TitleSalaryFork {

    private Title title;

    private BigDecimal minSalary;

    private BigDecimal maxSalary;

}
