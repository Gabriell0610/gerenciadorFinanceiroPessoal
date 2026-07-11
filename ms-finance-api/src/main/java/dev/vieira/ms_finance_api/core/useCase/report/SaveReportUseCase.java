package dev.vieira.ms_finance_api.core.useCase.report;

import dev.vieira.ms_finance_api.core.entities.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface SaveReportUseCase {

    Report execute(UUID reportId, UUID userId, LocalDate competency, BigDecimal totalExpenses, BigDecimal totalInstallments, BigDecimal grandTotal, int itemCount);
}
