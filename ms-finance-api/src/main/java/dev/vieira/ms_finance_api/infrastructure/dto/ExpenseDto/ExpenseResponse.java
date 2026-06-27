package dev.vieira.ms_finance_api.infrastructure.dto.ExpenseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ExpenseResponse(
        UUID id,
        String description,
        BigDecimal amount,
        String messageUser,
        LocalDateTime dateExpense,
        UUID userId
) {}
