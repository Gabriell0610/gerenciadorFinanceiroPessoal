package dev.vieira.ms_notification_api.service.NotificationResponseService;

import dev.vieira.ms_notification_api.dto.NotificationResponseDto;
import dev.vieira.ms_notification_api.resource.client.TelegramClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationResponseImpl implements NotificationResponseUseCase {

    private final TelegramClient telegramClient;

    @Override
    public void execute(NotificationResponseDto message) {
       try {
           telegramClient.sendMessage(message.chaId(), message.message());
       } catch (Exception e) {
           System.out.println("[Infra - Telegram] Erro ao enviar mensagem para o Telegram: " + e.getMessage());
       }
    }
}
