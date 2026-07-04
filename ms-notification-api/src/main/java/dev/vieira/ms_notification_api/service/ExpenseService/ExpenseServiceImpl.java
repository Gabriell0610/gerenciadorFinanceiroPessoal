package dev.vieira.ms_notification_api.service.ExpenseService;

import dev.vieira.ms_notification_api.dto.TelegramUpdateDto;
import dev.vieira.ms_notification_api.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static dev.vieira.ms_notification_api.config.RabbitMQConfig.ROUTING_KEY_EXPENSE;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl {
    private final MessageService messageService;

    public void execute(TelegramUpdateDto message) {

        System.out.println("Inicando processo gasto: " + message);
        // Envia a mensagem para a fila do RabbitMQ ou SQS da AWS quando for para produção
        messageService.sendMessage(ROUTING_KEY_EXPENSE, message);
    }
}
