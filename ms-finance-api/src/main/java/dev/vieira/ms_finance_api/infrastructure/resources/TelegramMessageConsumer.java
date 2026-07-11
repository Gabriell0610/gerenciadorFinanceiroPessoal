package dev.vieira.ms_finance_api.infrastructure.resources;

import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramUpdateDto;
import dev.vieira.ms_finance_api.core.enums.ProcessConsumer;
import dev.vieira.ms_finance_api.core.useCase.process.ProcessMessageUseCase;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static dev.vieira.ms_finance_api.infrastructure.config.RabbitMQConfig.QUEUE_EXPENSE;
import static dev.vieira.ms_finance_api.infrastructure.config.RabbitMQConfig.QUEUE_REPORT;

@Component
public class TelegramMessageConsumer {

    private final ProcessMessageUseCase processMessageUseCase;

    public TelegramMessageConsumer(ProcessMessageUseCase processMessageUseCase) {
        this.processMessageUseCase = processMessageUseCase;
    }

    @RabbitListener(queues = QUEUE_EXPENSE)
    public void processsExpense(TelegramUpdateDto payload) {
        try {
            System.out.println("[Infra - RabbitMQ - Expense] Mensagem capturada da fila!");

            // Direciona o fluxo para dentro do Core da aplicação
            this.processMessageUseCase.execute(payload, ProcessConsumer.EXPENSE);

        } catch (Exception e) {
            System.err.println("Erro no consumo da mensagem: " + e.getMessage());
        }
    }

    @RabbitListener(queues = QUEUE_REPORT)
    public void processsReport(TelegramUpdateDto payload) {
        try {
            System.out.println("[Infra - RabbitMQ - Report] Mensagem capturada da fila!");

            // Direciona o fluxo para dentro do Core da aplicação
            this.processMessageUseCase.execute(payload, ProcessConsumer.REPORT);

        } catch (Exception e) {
            System.err.println("Erro no consumo da mensagem: " + e.getMessage());
        }
    }
}
