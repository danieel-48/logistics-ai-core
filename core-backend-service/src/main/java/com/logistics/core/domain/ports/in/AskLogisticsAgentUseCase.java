package com.logistics.core.domain.ports.in;

import com.logistics.core.domain.model.LogisticsAgentResponse;

public interface AskLogisticsAgentUseCase {
    LogisticsAgentResponse ask(String prompt);
}