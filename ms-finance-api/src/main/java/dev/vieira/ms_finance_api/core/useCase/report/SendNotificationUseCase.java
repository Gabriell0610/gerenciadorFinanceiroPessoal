package dev.vieira.ms_finance_api.core.useCase.report;

import dev.vieira.ms_finance_api.core.dto.Report.NotificationResponseDto;

public interface SendNotificationUseCase {

    void execute(NotificationResponseDto message);
}
