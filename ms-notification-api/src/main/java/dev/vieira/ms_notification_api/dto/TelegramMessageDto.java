package dev.vieira.ms_notification_api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TelegramMessageDto(@NotNull Long message_id,
                                 @NotNull @Valid TelegramChatDto chat,
                                 @NotBlank(message = "O texto da mensagem não pode ser vazio") String text) {
}