package dev.vieira.ms_finance_api.core.useCase.expense;

import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;

import java.util.List;
import java.util.UUID;

public class FindAllExpenseByUserIdImpl implements FindAllExpenseByUserIdUseCase {

    private final FinanceGateway financeGateway;

    public FindAllExpenseByUserIdImpl(FinanceGateway financeGateway) {
        this.financeGateway = financeGateway;
    }

    @Override
    public List<Expense> execute(UUID userId) {
        return financeGateway.findAllExpenseByUserId(userId);
    }
}
