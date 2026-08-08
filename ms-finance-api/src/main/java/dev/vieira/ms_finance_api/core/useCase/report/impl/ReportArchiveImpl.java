package dev.vieira.ms_finance_api.core.useCase.report.impl;

import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.entities.Report;
import dev.vieira.ms_finance_api.core.useCase.report.contract.ReportArchiveUseCase;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ReportArchiveImpl implements ReportArchiveUseCase {


    @Override
    public byte[] process(Report report, List<Expense> monthlyExpense ) {
        StringBuilder builder = new StringBuilder();

        builder.append("Descrição,");
        builder.append("Preço,");
        builder.append("Parcela,");
        builder.append("Data em que gastou");
        builder.append("\n");

        for (Expense expense : monthlyExpense) {
            builder.append(expense.getDescription()).append(",")
                    .append(expense.getAmount().toString()).append(",")
                    .append(expense.getInstallment().toString()).append(",")
                    .append(expense.getDateExpense().toString())
                    .append("\n");
        }


        return builder.toString().getBytes(StandardCharsets.UTF_8);
    }
}
