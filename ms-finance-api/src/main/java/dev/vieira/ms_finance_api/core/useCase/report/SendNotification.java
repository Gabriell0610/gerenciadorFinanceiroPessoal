package dev.vieira.ms_finance_api.core.useCase.report;

import dev.vieira.ms_finance_api.core.dto.Report.NotificationResponseDto;
import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;

public class SendNotification implements SendNotificationUseCase {

    private FinanceGateway financeGateway;

    public SendNotification(FinanceGateway financeGateway) {
        this.financeGateway = financeGateway;
    }

    @Override
    public void execute(NotificationResponseDto message) {
        this.financeGateway.sendMessage(message);
    }
}
