package dev.vieira.ms_finance_api.core.usecases.report;

import dev.vieira.ms_finance_api.core.entities.Report;
import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class FindReportByUserAndCompetencyImpl implements FindReportByUserAndCompetencyUseCase {

    private final FinanceGateway financeGateway;

    public FindReportByUserAndCompetencyImpl(FinanceGateway financeGateway) {
        this.financeGateway = financeGateway;
    }

    @Override
    public Optional<Report> execute(UUID userId, LocalDate competency) {
        return financeGateway.findReportByUserAndCompetency(userId, competency);
    }
}
