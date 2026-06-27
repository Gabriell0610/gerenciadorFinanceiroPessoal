package dev.vieira.ms_finance_api.core.usecases;

import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramUpdateDto;

public interface ProcessMessageUseCase {

    void execute(TelegramUpdateDto message);
}
