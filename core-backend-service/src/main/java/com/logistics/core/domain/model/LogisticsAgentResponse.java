package com.logistics.core.domain.model;

import java.util.List;
import java.util.Map;

public record LogisticsAgentResponse(
    String prompt,
    String answer,
    List<ActionStep> stepsExecuted
) {
    public record ActionStep(
        String toolName,
        Map<String, Object> arguments,
        Object output
    ) {}
}