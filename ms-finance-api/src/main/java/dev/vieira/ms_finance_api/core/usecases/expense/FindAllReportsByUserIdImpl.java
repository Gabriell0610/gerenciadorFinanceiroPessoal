package dev.vieira.ms_finance_api.core.usecases.expense;

import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;

import java.util.List;
import java.util.UUID;

public class FindAllReportsByUserIdImpl implements FindAllReportsByUserIdUseCase {

    private final FinanceGateway financeGateway;

    public FindAllReportsByUserIdImpl(FinanceGateway financeGateway) {
        this.financeGateway = financeGateway;
    }

    @Override
    public List<Expense> execute(UUID userId) {
        return financeGateway.findAllReportsByUserId(userId);
    }
}
