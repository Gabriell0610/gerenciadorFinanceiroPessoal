package dev.vieira.ms_finance_api.core.useCase.report.impl;

import dev.vieira.ms_finance_api.core.entities.Report;
import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;
import dev.vieira.ms_finance_api.core.useCase.report.contract.FindReportByUserAndCompetencyUseCase;

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
