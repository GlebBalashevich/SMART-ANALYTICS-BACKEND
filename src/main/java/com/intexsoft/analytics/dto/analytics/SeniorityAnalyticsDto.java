package com.intexsoft.analytics.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeniorityAnalyticsDto {

    private String departmentName;

    private Double seniorityIndex;

}
