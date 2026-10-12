package dev.vieira.ms_finance_api.core.gateway;

import dev.vieira.ms_finance_api.infrastructure.dto.GeminiDto.GeminiResponseDto;

public interface PromptFinanceGateway {

    GeminiResponseDto processMessage(String message);
}
