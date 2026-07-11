package dev.vieira.ms_finance_api.core.useCase.expense;

import dev.vieira.ms_finance_api.core.dto.Expense.ParsedExpenseDto;
import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramUpdateDto;
import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.entities.User;

public interface SaveExpenseUseCase  {

    Expense execute(User user, TelegramUpdateDto payload, ParsedExpenseDto parsed);
}
