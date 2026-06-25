package dev.vieira.ms_notification_api.service.TelegramService;

import dev.vieira.ms_notification_api.dto.TelegramUpdateDto;
import dev.vieira.ms_notification_api.service.MessageService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

import static dev.vieira.ms_notification_api.config.RabbitMQConfig.ROUTING_KEY_TELEGRAM;

@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {

    private final Validator validator;
    private final MessageService messageService;

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

        // Envia a mensagem para a fila do RabbitMQ ou SQS da AWS quando for para produção
        messageService.sendMessage(ROUTING_KEY_TELEGRAM, message);
    }
}
