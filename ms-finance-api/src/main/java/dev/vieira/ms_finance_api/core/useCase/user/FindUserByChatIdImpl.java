package dev.vieira.ms_finance_api.core.useCase.user;

import dev.vieira.ms_finance_api.core.entities.User;
import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;

import java.util.Optional;

public class FindUserByChatIdImpl implements FindUserByChatIdUseCase {

    private final FinanceGateway financeGateway;

    public FindUserByChatIdImpl(FinanceGateway financeGateway) {
        this.financeGateway = financeGateway;
    }

    @Override
    public Optional<User> execute(Long id) {

        return financeGateway.findUserByChatId(id);

    }
}
