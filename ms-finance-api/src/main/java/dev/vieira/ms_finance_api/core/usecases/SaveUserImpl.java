package dev.vieira.ms_finance_api.core.usecases;

import dev.vieira.ms_finance_api.core.entities.User;
import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;

public class SaveUserImpl implements SaveUserUseCase {

    private final FinanceGateway financeGateway;

    public SaveUserImpl(FinanceGateway financeGateway) {
        this.financeGateway = financeGateway;
    }

    @Override
    public User execute(User user) {
        var saveUser = financeGateway.saveUser(user);
        return saveUser;
    }

}
