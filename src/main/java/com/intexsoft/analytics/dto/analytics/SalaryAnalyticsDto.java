package com.intexsoft.analytics.dto.analytics;

import java.math.BigDecimal;

import com.intexsoft.analytics.model.SelectionCriteria;
import com.intexsoft.analytics.model.Title;
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

    private Title title;

    private SelectionCriteria selectionCriteria;

    private BigDecimal salaryValue;

}
