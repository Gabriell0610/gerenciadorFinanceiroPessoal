package dev.vieira.ms_finance_api.core.useCase.expense;

import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class FindAllExpenseByUserIdWithCompetencyImpl implements FindAllExpenseByUserIdWithCompetencyUseCase {

    private final FinanceGateway financeGateway;

    public FindAllExpenseByUserIdWithCompetencyImpl(FinanceGateway financeGateway) {
        this.financeGateway = financeGateway;
    }

    @Override
    public List<Expense> execute(UUID userId, LocalDate competency) {
        return financeGateway.findAllUserExpenseWithCompetency(userId, competency);
    }
}
