package dev.vieira.ms_finance_api.core.useCase.process;

import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramUpdateDto;
import dev.vieira.ms_finance_api.core.enums.ProcessConsumer;

import java.io.IOException;

public interface ProcessMessageUseCase {

    void execute(TelegramUpdateDto message, ProcessConsumer process) throws IOException;
}
