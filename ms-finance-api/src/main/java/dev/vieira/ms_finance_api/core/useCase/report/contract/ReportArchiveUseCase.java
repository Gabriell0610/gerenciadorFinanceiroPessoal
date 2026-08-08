package dev.vieira.ms_finance_api.core.useCase.report.contract;

import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.entities.Report;

import java.util.List;

public interface ReportArchiveUseCase {

    byte[] process(Report report, List<Expense> monthlyExpenses);
}
