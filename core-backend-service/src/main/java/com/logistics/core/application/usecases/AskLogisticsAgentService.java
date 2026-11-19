package com.logistics.core.application.usecases;

import com.logistics.core.domain.model.LogisticsAgentResponse;
import com.logistics.core.domain.ports.in.AskLogisticsAgentUseCase;
import com.logistics.core.domain.ports.out.LogisticsAgentPort;
import org.springframework.stereotype.Service;

@Service
public class AskLogisticsAgentService implements AskLogisticsAgentUseCase {

    private final LogisticsAgentPort logisticsAgentPort;

    public AskLogisticsAgentService(LogisticsAgentPort logisticsAgentPort) {
        this.logisticsAgentPort = logisticsAgentPort;
    }

    @Override
    public LogisticsAgentResponse ask(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            throw new IllegalArgumentException("La consulta al agente de logística no puede ser nula ni vacía.");
        }
        return logisticsAgentPort.askAgent(prompt.trim());
    }
}