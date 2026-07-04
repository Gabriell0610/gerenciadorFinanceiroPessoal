package dev.vieira.ms_finance_api.core.usecases.expense;

import dev.vieira.ms_finance_api.core.entities.Expense;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FindAllReportsByUserIdUseCase {

    List<Expense> execute(UUID userId);
}
