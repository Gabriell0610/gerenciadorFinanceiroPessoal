package dev.vieira.ms_notification_api.service.TelegramService;

import dev.vieira.ms_notification_api.dto.TelegramUpdateDto;

public interface TelegramService {

    void processTelegramMessage(TelegramUpdateDto message);
}
