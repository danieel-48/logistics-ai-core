package com.logistics.core.application.dto;

import com.logistics.core.domain.model.LogisticsAgentResponse;

import java.util.List;
import java.util.Map;

public record AgentChatResponseDto(
    String prompt,
    String answer,
    List<ActionStepDto> stepsExecuted
) {
    public record ActionStepDto(
        String toolName,
        Map<String, Object> arguments,
        Object output
    ) {}

    public static AgentChatResponseDto fromDomain(LogisticsAgentResponse domain) {
        List<ActionStepDto> steps = (domain.stepsExecuted() == null) ? List.of() : domain.stepsExecuted().stream()
                .map(s -> new ActionStepDto(
                        s.toolName(),
                        s.arguments(),
                        s.output()
                ))
                .toList();

        return new AgentChatResponseDto(
                domain.prompt(),
                domain.answer(),
                steps
        );
    }
}