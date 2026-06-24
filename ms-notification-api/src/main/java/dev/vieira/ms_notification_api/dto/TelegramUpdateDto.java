package dev.vieira.ms_notification_api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TelegramUpdateDto(
        @NotNull Long update_id,
        @NotNull @Valid TelegramMessageDto message
) {}
