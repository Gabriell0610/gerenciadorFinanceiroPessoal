package dev.vieira.ms_finance_api.core.dto.TelegramDto;

public record TelegramUpdateDto(
       Long update_id,
       TelegramMessageDto message
) {}
