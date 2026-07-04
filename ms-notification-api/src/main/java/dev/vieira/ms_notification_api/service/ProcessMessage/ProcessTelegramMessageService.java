package dev.vieira.ms_notification_api.service.ProcessMessage;

import dev.vieira.ms_notification_api.dto.TelegramUpdateDto;

public interface ProcessTelegramMessageService {

    void process(TelegramUpdateDto message);
}
