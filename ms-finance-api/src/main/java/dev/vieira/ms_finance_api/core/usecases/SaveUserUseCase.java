package dev.vieira.ms_finance_api.core.usecases;

import dev.vieira.ms_finance_api.core.entities.User;

public interface SaveUserUseCase {

    User execute(User user);
}
