package dev.vieira.ms_notification_api.service.TelegramService;

import dev.vieira.ms_notification_api.dto.TelegramUpdateDto;
import jakarta.validation.ConstraintViolation;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

import static dev.vieira.ms_notification_api.config.RabbitMQConfig.EXCHANGE_TELEGRAM;
import static dev.vieira.ms_notification_api.config.RabbitMQConfig.ROUTING_KEY_TELEGRAM;

@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {

    private final RabbitTemplate rabbitTemplate;
    private final Validator validator;

    @Override
    public void processTelegramMessage(TelegramUpdateDto message) {

        System.out.println("DTO: " + message);

        Set<ConstraintViolation<TelegramUpdateDto>> violations = validator.validate(message);

        if (!violations.isEmpty()) {

            for (ConstraintViolation<TelegramUpdateDto> erro : violations) {
                System.out.println("Erro encontrado: " + erro.getMessage());
            }

            return;
        }

        rabbitTemplate.convertAndSend(
                EXCHANGE_TELEGRAM,
                ROUTING_KEY_TELEGRAM,
                message
        );
    }
}
