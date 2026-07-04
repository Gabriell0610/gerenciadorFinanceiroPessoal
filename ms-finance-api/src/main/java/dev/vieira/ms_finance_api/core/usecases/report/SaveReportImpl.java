package dev.vieira.ms_finance_api.core.usecases.report;

import dev.vieira.ms_finance_api.core.entities.Report;
import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class SaveReportImpl implements SaveReportUseCase {

    private final FinanceGateway financeGateway;

    public SaveReportImpl(FinanceGateway financeGateway) {
        this.financeGateway = financeGateway;
    }

    @Override
    public Report execute(UUID reportId, UUID userId, LocalDate competency, BigDecimal totalExpenses, BigDecimal totalInstallments, BigDecimal grandTotal, int itemCount) {
        var report = new Report(reportId, userId, competency, totalExpenses, totalInstallments, grandTotal, itemCount);
        return financeGateway.saveReport(report);
    }
}
