package dev.vieira.ms_finance_api.core.usecases;

import dev.vieira.ms_finance_api.core.entities.Expense;

public interface SaveExpenseUseCase  {

    Expense execute(Expense expense);
}
