package com.intexsoft.analytics.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Title {

    JUNIOR(1),

    MIDDLE(2),

    SENIOR(3),

    MANAGER(4);

    private final int seniorityIndex;
}
