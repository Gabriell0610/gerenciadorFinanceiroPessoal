package dev.vieira.ms_finance_api.infrastructure.gateway;

import dev.vieira.ms_finance_api.core.dto.Expense.ParsedExpenseDto;
import dev.vieira.ms_finance_api.core.exception.BusinessException;
import dev.vieira.ms_finance_api.core.gateway.ParsedMessageGateway;
import dev.vieira.ms_finance_api.core.gateway.PromptFinanceGateway;
import dev.vieira.ms_finance_api.infrastructure.dto.GeminiDto.GeminiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class ParsedMessageGeminiGatewayImpl implements ParsedMessageGateway {

    private final PromptFinanceGateway promptFinanceGateway;
    private final ObjectMapper objectMapper;

    @Override
    public ParsedExpenseDto parse(String message) {
        GeminiResponseDto geminiResponse  = this.promptFinanceGateway.processMessage(message);

        String json = geminiResponse.candidates()
                .get(0)
                .content()
                .parts()
                .get(0)
                .text();

        objectMapper.registeredModules();
        ParsedExpenseDto parsed = objectMapper.readValue(json, ParsedExpenseDto.class);

        if (parsed.description().isEmpty()) {
            throw new BusinessException("Mensagem inválida, não foi possível parsear o gasto.");
        }

        return parsed;
    }
}
