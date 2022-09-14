package com.intexsoft.analytics.dto.employee;

import java.math.BigDecimal;

import com.intexsoft.analytics.model.Title;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TitleSalaryForkDto {

    private Title title;

    private BigDecimal minSalary;

    private BigDecimal maxSalary;

}
