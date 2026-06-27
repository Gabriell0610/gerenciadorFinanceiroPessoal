package dev.vieira.ms_finance_api.core.dto.TelegramDto;

public record TelegramMessageDto( Long message_id,
                                 TelegramChatDto chat,
                                  String text) {
}