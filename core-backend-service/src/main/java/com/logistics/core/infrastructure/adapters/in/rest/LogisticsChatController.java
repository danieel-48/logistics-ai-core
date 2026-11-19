package com.logistics.core.infrastructure.adapters.in.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.core.application.dto.AgentChatRequestDto;
import com.logistics.core.application.dto.AgentChatResponseDto;
import com.logistics.core.domain.ports.in.AskLogisticsAgentUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/logistics")
@Tag(name = "AI Logistics Agent", description = "Gateway conversacional de comercio exterior")
public class LogisticsChatController {

    private final AskLogisticsAgentUseCase askLogisticsAgentUseCase;

    public LogisticsChatController(AskLogisticsAgentUseCase askLogisticsAgentUseCase) {
        this.askLogisticsAgentUseCase = askLogisticsAgentUseCase;
    }

    @PostMapping("/chat")
    @Operation(summary = "Enviar consulta en lenguaje natural sobre importaciones y aduanas")
    public ResponseEntity<AgentChatResponseDto> chatWithAgent(@Valid @RequestBody AgentChatRequestDto request) {
        AgentChatResponseDto response = AgentChatResponseDto.fromDomain(
                askLogisticsAgentUseCase.ask(request.prompt())
        );
        return ResponseEntity.ok(response);
    }
}