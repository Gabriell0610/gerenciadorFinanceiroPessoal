package dev.vieira.ms_finance_api.core.dto.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ParsedExpenseDto(
        String description,
        BigDecimal amount,
        String category,
        int installments,
        LocalDate paymentDate) {
    
}