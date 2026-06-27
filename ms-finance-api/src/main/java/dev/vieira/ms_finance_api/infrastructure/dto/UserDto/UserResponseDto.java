package dev.vieira.ms_finance_api.infrastructure.dto.UserDto;


import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String name,
        String email,
        String telegramId,
        Long chatId,
        LocalDateTime createdAt
) {}