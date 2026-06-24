package dev.vieira.ms_notification_api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@JsonIgnoreProperties(ignoreUnknown = true)
public record TelegramChatDto(@NotNull
                              Long id, // id do chat para caso eu quero enviar mensagem para o usuário de volta
                              String first_name,
                              String type) {
}
