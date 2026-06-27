package dev.vieira.ms_finance_api.infrastructure.dto.UserDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequest(
        String name,
        String email,

        @NotBlank(message = "O Telegram ID é obrigatório")
        String telegramId,

        @NotNull(message = "O Chat ID é obrigatório")
        Long chatId
) {}
