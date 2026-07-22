package dev.vieira.ms_notification_api.resource.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TelegramClient {

    private final TelegramFeignClient feignClient;

    @Value("${telegram_token}")
    private String botToken;

    public void sendMessage(Long chatId, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("chat_id", chatId);
        body.put("text", message);
        body.put("parse_mode", "HTML");

        feignClient.sendMessage(botToken, body);
    }
}
