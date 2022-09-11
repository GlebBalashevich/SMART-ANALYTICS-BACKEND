package com.intexsoft.analytics.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JobRole {

    SOFTWARE_ENGINEER("software engineer"),

    SOFTWARE_TESTING_ENGINEER("software testing engineer"),

    DEPARTMENT_MANAGER("department manager"),

    HR("hr");

    private final String name;
}
