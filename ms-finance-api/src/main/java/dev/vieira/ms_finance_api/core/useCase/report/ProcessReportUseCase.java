package dev.vieira.ms_finance_api.core.useCase.report;

import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramUpdateDto;

public interface ProcessReportUseCase {

    void execute(TelegramUpdateDto payload);
}
