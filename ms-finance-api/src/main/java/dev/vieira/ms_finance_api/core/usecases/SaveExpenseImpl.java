package dev.vieira.ms_finance_api.core.usecases;

import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;

public class SaveExpenseImpl implements SaveExpenseUseCase {

    private final FinanceGateway financeGateway;

    public SaveExpenseImpl(FinanceGateway financeGateway) {
        this.financeGateway = financeGateway;
    }

    @Override
    public Expense execute(Expense expense) {
        return null;
    }
}
