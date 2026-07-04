package dev.vieira.ms_finance_api.core.usecases.user;

import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramUpdateDto;
import dev.vieira.ms_finance_api.core.entities.User;
import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;

public class SaveUserImpl implements SaveUserUseCase {

    private final FinanceGateway financeGateway;

    public SaveUserImpl(FinanceGateway financeGateway) {
        this.financeGateway = financeGateway;
    }

    @Override
    public User execute(TelegramUpdateDto payload) {
        User user = new User(payload.message().chat().first_name(), null, payload.message().chat().id());

        return financeGateway.saveUser(user);

    }

}
