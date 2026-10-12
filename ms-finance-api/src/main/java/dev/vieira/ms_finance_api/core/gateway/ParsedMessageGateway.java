package dev.vieira.ms_finance_api.core.gateway;

import dev.vieira.ms_finance_api.core.dto.Expense.ParsedExpenseDto;

public interface ParsedMessageGateway {

    ParsedExpenseDto parse(String message);
}
