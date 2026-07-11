package dev.vieira.ms_finance_api.core.useCase.user;

import dev.vieira.ms_finance_api.core.entities.User;

import java.util.Optional;

public interface FindUserByChatIdUseCase {

    Optional<User> execute(Long id);
}
