package dev.vieira.ms_notification_api.service.NotificationResponseService;

import dev.vieira.ms_notification_api.dto.NotificationResponseDto;

public interface NotificationResponseUseCase {
    void execute (NotificationResponseDto message);
}
