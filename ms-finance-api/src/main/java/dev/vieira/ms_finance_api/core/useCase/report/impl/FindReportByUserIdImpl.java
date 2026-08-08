package dev.vieira.ms_finance_api.core.useCase.report.impl;

import dev.vieira.ms_finance_api.core.entities.Report;
import dev.vieira.ms_finance_api.core.useCase.report.contract.FindReportByUserIdUseCase;

import java.util.UUID;

public class FindReportByUserIdImpl implements FindReportByUserIdUseCase {

    @Override
    public Report execute(UUID userId) {
        return null;
    }
}
