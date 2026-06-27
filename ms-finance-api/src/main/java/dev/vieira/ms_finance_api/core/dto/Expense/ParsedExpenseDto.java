package dev.vieira.ms_finance_api.core.dto.Expense;

import java.math.BigDecimal;

public record ParsedExpenseDto(
        String nome,
        BigDecimal valor,
        Integer parcelas) {
    
}