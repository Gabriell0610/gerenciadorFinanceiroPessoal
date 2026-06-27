package dev.vieira.ms_finance_api.infrastructure.dto.ExpenseDto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public record expenseCreateRequest(
        @NotBlank(message = "A mensagem original do usuário é obrigatória")
        String messageUser,

        @NotNull(message = "O valor do gasto é obrigatório")
        @Positive(message = "O valor deve ser maior que zero")
        BigDecimal amount,

        @NotBlank(message = "A descrição do gasto é obrigatória")
        String description,

        @NotNull(message = "O ID do usuário é obrigatório")
        UUID userId
) {}
