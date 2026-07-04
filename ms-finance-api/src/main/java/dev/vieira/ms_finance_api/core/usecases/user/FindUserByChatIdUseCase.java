package dev.vieira.ms_finance_api.core.usecases.user;

import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramUpdateDto;
import dev.vieira.ms_finance_api.core.entities.User;

import java.util.Optional;

public interface FindUserByChatIdUseCase {

    Optional<User> execute(Long id);
}
