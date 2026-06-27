package dev.vieira.ms_finance_api.core.dto.TelegramDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public record TelegramChatDto(
                              Long id, // id do chat para caso eu quero enviar mensagem para o usuário de volta
                              String first_name,
                              String type) {
}
