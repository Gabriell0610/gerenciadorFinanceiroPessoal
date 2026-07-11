package dev.vieira.ms_notification_api.resource.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "telegram", url = "https://api.telegram.org")
public interface TelegramFeignClient {

    @PostMapping("/bot{token}/sendMessage")
    void sendMessage(
            @PathVariable("token") String token,
            @RequestBody Map<String, Object> body
    );
}