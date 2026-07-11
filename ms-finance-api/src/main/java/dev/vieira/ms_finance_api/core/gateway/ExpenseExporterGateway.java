package dev.vieira.ms_finance_api.core.gateway;

import dev.vieira.ms_finance_api.core.entities.Expense;

public interface ExpenseExporterGateway {

    void export(Expense expense);
}
