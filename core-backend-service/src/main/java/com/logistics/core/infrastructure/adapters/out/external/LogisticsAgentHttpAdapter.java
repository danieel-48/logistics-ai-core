package com.logistics.core.infrastructure.adapters.out.external;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.logistics.core.domain.model.LogisticsAgentResponse;
import com.logistics.core.domain.ports.out.LogisticsAgentPort;

@Component
public class LogisticsAgentHttpAdapter implements LogisticsAgentPort {

    private final RestClient restClient;

    public LogisticsAgentHttpAdapter(RestClient aiAgentRestClient) {
        this.restClient = aiAgentRestClient;
    }

    // Records internos para mapear la respuesta HTTP del microservicio Python
    record PythonAgentRequest(String prompt) {}

    record PythonActionStep(
        @JsonProperty("tool_name") String toolName,
        Map<String, Object> arguments,
        Object output
    ) {}

    record PythonAgentResponse(
        String prompt,
        String answer,
        @JsonProperty("steps_executed") List<PythonActionStep> stepsExecuted
    ) {}

    @Override
    public LogisticsAgentResponse askAgent(String prompt) {
        PythonAgentResponse response = restClient.post()
                .uri("/api/v1/logistics/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PythonAgentRequest(prompt))
                .retrieve()
                .body(PythonAgentResponse.class);

        if (response == null) {
            throw new IllegalStateException("El servicio de agente de IA no devolvió respuesta.");
        }

        List<LogisticsAgentResponse.ActionStep> steps = (response.stepsExecuted() == null) ? List.of() :
                response.stepsExecuted().stream()
                        .map(s -> new LogisticsAgentResponse.ActionStep(
                                s.toolName(),
                                s.arguments(),
                                s.output()
                        ))
                        .toList();

        return new LogisticsAgentResponse(
                response.prompt(),
                response.answer(),
                steps
        );
    }
}