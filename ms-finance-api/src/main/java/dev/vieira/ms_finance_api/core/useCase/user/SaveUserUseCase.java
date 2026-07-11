package dev.vieira.ms_finance_api.core.useCase.user;

import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramUpdateDto;
import dev.vieira.ms_finance_api.core.entities.User;

public interface SaveUserUseCase {

    User execute(TelegramUpdateDto payload);
}
