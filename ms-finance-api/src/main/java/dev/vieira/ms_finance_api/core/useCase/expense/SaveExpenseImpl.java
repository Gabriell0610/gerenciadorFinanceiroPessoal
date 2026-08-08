package dev.vieira.ms_finance_api.core.useCase.expense;

import dev.vieira.ms_finance_api.core.dto.Expense.ParsedExpenseDto;
import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramUpdateDto;
import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.entities.User;
import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;

import java.time.LocalDateTime;

public class SaveExpenseImpl implements SaveExpenseUseCase {

    private final FinanceGateway financeGateway;

    public SaveExpenseImpl(FinanceGateway financeGateway) {
        this.financeGateway = financeGateway;
    }

    @Override
    public Expense execute(User user, TelegramUpdateDto payload, ParsedExpenseDto parsed) {

        var installment = 1;
        if(parsed.installments() > 1) {
            installment = parsed.installments();
        }


        var expenseEntity = new Expense(user.getId(), parsed.amount(),
                payload.message().text(), parsed.description(), installment, parsed.category(), parsed.paymentDate());
        return financeGateway.saveExpense(expenseEntity);

    }
}
