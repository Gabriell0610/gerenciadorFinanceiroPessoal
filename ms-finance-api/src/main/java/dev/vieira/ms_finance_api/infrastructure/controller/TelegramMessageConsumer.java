package dev.vieira.ms_finance_api.infrastructure.controller;

import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramUpdateDto;
import dev.vieira.ms_finance_api.core.usecases.ProcessMessageUseCase;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static dev.vieira.ms_finance_api.infrastructure.config.RabbitMQConfig.QUEUE_TELEGRAM;

@Component
public class TelegramMessageConsumer {

    private final ProcessMessageUseCase processMessageUseCase;

    public TelegramMessageConsumer(ProcessMessageUseCase processMessageUseCase) {
        this.processMessageUseCase = processMessageUseCase;
    }

    @RabbitListener(queues = QUEUE_TELEGRAM)
    public void processs(TelegramUpdateDto payload) {
        try {
            System.out.println("[Infra - RabbitMQ] Mensagem capturada da fila!");

            // Direciona o fluxo para dentro do Core da aplicação
            this.processMessageUseCase.execute(payload);

        } catch (Exception e) {
            System.err.println("Erro no consumo da mensagem: " + e.getMessage());
        }
    }
}
