package dev.vieira.ms_notification_api.resource.NotificationConsumer;

import dev.vieira.ms_notification_api.dto.NotificationResponseDto;
import dev.vieira.ms_notification_api.service.NotificationResponseService.NotificationResponseUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static dev.vieira.ms_notification_api.config.RabbitMQConfig.QUEUE_NOTIFICATION_RESPONSE;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationResponseUseCase notificationResponseUseCase;

    @RabbitListener(queues = QUEUE_NOTIFICATION_RESPONSE)
    public void processsReport(NotificationResponseDto payload) {
        try {
            System.out.println("[Infra - RabbitMQ - Response report] Mensagem capturada da fila!");

            // Direciona o fluxo para dentro do Core da aplicação
            this.notificationResponseUseCase.execute(payload);

        } catch (Exception e) {
            System.err.println("Erro no consumo da mensagem: " + e.getMessage());
        }
    }
}
