package com.intexsoft.analytics.dto.analytics;

import java.util.List;

import com.intexsoft.analytics.dto.employee.TitleSalaryForkDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaryAnalyticsDto {

    private List<TitleSalaryForkDto> titleSalaryForks;

}
