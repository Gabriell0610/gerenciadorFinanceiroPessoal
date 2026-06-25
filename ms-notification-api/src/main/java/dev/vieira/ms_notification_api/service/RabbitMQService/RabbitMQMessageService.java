package dev.vieira.ms_notification_api.service.RabbitMQService;

import dev.vieira.ms_notification_api.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static dev.vieira.ms_notification_api.config.RabbitMQConfig.EXCHANGE_TELEGRAM;

@Service
@Profile("dev")
@RequiredArgsConstructor
public class RabbitMQMessageService implements MessageService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendMessage(String destination, Object message) {
        System.out.println("[RabbitMQ] Enviando mensagem para a exchange/routing key: " + destination);
        rabbitTemplate.convertAndSend(
                EXCHANGE_TELEGRAM,
                destination,
                message

        );
    }
}
