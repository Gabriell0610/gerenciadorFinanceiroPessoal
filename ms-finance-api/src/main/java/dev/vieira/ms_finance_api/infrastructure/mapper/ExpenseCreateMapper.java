package dev.vieira.ms_finance_api.infrastructure.mapper;

import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.infrastructure.dto.ExpenseDto.expenseCreateRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ExpenseCreateMapper {

    public expenseCreateRequest toDto(Expense expense) {
        return new expenseCreateRequest(
                expense.getDescription(),
                expense.getAmount(),
                expense.getMessageUser(),
                expense.getUserId()
        );

    }

    public Expense toEntity(expenseCreateRequest expenseCreateRequest) {
        return new Expense(
                expenseCreateRequest.userId(),
                expenseCreateRequest.amount(),
                expenseCreateRequest.messageUser(),
                expenseCreateRequest.description()
        );

    }


}
