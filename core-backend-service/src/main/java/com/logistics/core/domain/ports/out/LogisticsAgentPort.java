package com.logistics.core.domain.ports.out;

import com.logistics.core.domain.model.LogisticsAgentResponse;

public interface LogisticsAgentPort {
    LogisticsAgentResponse askAgent(String prompt);
}