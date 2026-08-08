package dev.vieira.ms_finance_api.core.useCase.report.contract;

import dev.vieira.ms_finance_api.core.entities.Report;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface FindReportByUserAndCompetencyUseCase {

    Optional<Report> execute(UUID userId, LocalDate competency);
}
