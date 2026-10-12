package dev.vieira.ms_finance_api.core.useCase.expense;

import dev.vieira.ms_finance_api.core.entities.Expense;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface FindAllExpenseByUserIdWithCompetencyUseCase {

    List<Expense> execute(UUID userId, LocalDate competency);
}
