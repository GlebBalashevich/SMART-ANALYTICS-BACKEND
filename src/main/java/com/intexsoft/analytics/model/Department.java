package com.intexsoft.analytics.model;

import java.math.BigDecimal;
import java.time.Instant;
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
public class Department {

    @Id
    private UUID id;

    private String name;

    private BigDecimal salaryBudget;

    @CreatedDate
    private Instant createDate;

    @LastModifiedDate
    private Instant lastUpdateDate;

}
