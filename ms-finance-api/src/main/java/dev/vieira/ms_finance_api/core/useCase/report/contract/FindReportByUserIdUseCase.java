package dev.vieira.ms_finance_api.core.useCase.report.contract;

import dev.vieira.ms_finance_api.core.entities.Report;

import java.util.UUID;

public interface FindReportByUserIdUseCase {

    Report execute(UUID userId);
}
