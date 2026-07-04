package dev.vieira.ms_notification_api.service.ReportService;

import dev.vieira.ms_notification_api.dto.TelegramUpdateDto;
import dev.vieira.ms_notification_api.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static dev.vieira.ms_notification_api.config.RabbitMQConfig.ROUTING_KEY_REPORT;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl {
    private final MessageService messageService;

    public void execute(TelegramUpdateDto message) {
        System.out.println("Inicando processo relatorio: " + message);
        messageService.sendMessage(ROUTING_KEY_REPORT, message);

    }
}
