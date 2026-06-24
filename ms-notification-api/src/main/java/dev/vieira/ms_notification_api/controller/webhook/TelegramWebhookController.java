package dev.vieira.ms_notification_api.controller.webhook;


import dev.vieira.ms_notification_api.dto.TelegramUpdateDto;
import dev.vieira.ms_notification_api.service.TelegramService.TelegramService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/webhooks")
public class TelegramWebhookController {

    private final TelegramService telegramService;

    public TelegramWebhookController(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @PostMapping("/telegram")
    public ResponseEntity<Void> receiveUpdate(@RequestBody TelegramUpdateDto dto) {
        telegramService.processTelegramMessage(dto); // 1. Processa a mensagem recebida do Telegram
        return ResponseEntity.ok().build();
    }
}
