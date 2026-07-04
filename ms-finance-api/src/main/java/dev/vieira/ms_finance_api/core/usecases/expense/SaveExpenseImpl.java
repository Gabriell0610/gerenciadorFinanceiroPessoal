package dev.vieira.ms_finance_api.core.usecases.expense;

import dev.vieira.ms_finance_api.core.dto.Expense.ParsedExpenseDto;
import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramUpdateDto;
import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.entities.User;
import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;

public class SaveExpenseImpl implements SaveExpenseUseCase {

    private final FinanceGateway financeGateway;

    public SaveExpenseImpl(FinanceGateway financeGateway) {
        this.financeGateway = financeGateway;
    }

    @Override
    public Expense execute(User user, TelegramUpdateDto payload, ParsedExpenseDto parsed) {
        //regra de negocio antes de salvar
        var installment = 1;
        if(parsed.parcelas() > 1) {
            installment = parsed.parcelas();
        }
        var expenseEntity = new Expense(user.getId(), parsed.valor(), payload.message().text(), parsed.nome(), installment);
        return financeGateway.saveExpense(expenseEntity);
    }
}
