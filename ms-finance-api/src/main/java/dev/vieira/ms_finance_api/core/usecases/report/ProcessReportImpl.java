package dev.vieira.ms_finance_api.core.usecases.report;

import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramUpdateDto;
import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.usecases.expense.FindAllReportsByUserIdUseCase;
import dev.vieira.ms_finance_api.core.usecases.user.FindUserByChatIdUseCase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

public class ProcessReportImpl implements  ProcessReportUseCase{

    private final FindUserByChatIdUseCase findUserByChatIdUseCase;
    private final FindAllReportsByUserIdUseCase findAllReportsByUserIdUseCase;
    private final FindReportByUserAndCompetencyUseCase findReportByUserAndCompetencyUseCase;
    private final SaveReportUseCase saveReportUseCase;

    public ProcessReportImpl(FindUserByChatIdUseCase findUserByChatIdUseCase,  FindAllReportsByUserIdUseCase findAllReportsByUserIdUseCase, SaveReportUseCase saveReportUseCase,  FindReportByUserAndCompetencyUseCase findReportByUserAndCompetencyUseCase) {
        this.findUserByChatIdUseCase = findUserByChatIdUseCase;
        this.findAllReportsByUserIdUseCase = findAllReportsByUserIdUseCase;
        this.saveReportUseCase = saveReportUseCase;
        this.findReportByUserAndCompetencyUseCase = findReportByUserAndCompetencyUseCase;
    }

    @Override
    public void execute(TelegramUpdateDto payload) {
        System.out.println("Processando relatório para usuario");

        var user = findUserByChatIdUseCase.execute(payload.message().chat().id());

        if(user.isPresent()) {
            System.out.println("Usuario encontrado: " + user.get().getName());
            System.out.println("Gerando relatório para o usuario: " + user.get().getName());

            LocalDate competency = LocalDate.now(ZoneId.of("America/Sao_Paulo"))
                    .withDayOfMonth(1);


            var allExpenses = this.findAllReportsByUserIdUseCase.execute(user.get().getId());
            System.out.println("Gastos do usuário:  " + allExpenses);

            //filtra só os que aparecem no mês da competência
            var monthlyExpenses
                    = allExpenses.stream()
                    .filter(expense -> {
                        long periodoPasadoEntreMesDoGastoEMesDoReport = Period.between(
                                expense.getCreated_at().toLocalDate().withDayOfMonth(1),
                                competency
                        ).toTotalMonths();

                        return periodoPasadoEntreMesDoGastoEMesDoReport >= 0 && periodoPasadoEntreMesDoGastoEMesDoReport < expense.getInstallment();
                    })
                    .toList();

            var simpleExpense = monthlyExpenses.stream()
                    .filter(e -> e.getInstallment() == 1)
                    .toList();

            var expensesInstallments = monthlyExpenses.stream()
                    .filter(e -> e.getInstallment() > 1)
                    .toList();

            BigDecimal totalExpenses = simpleExpense.stream()
                    .map(Expense::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalInstallments = expensesInstallments.stream()
                    .map(Expense::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal grandTotal = totalExpenses.add(totalInstallments);
            int itemCount = monthlyExpenses.size();


            // Upsert do relário
            var existingReport = findReportByUserAndCompetencyUseCase.execute(user.get().getId(), competency);


            if(existingReport.isPresent()) {
                System.out.println("Relatório já existe para o usuário e competência, atualizando...");
                saveReportUseCase.execute(existingReport.get().getId(), user.get().getId(), competency, totalExpenses, totalInstallments, grandTotal, itemCount);
            } else {
                System.out.println("Relatório não existe para o usuário e competência, criando novo...");

                System.out.println("Total simples: " + totalExpenses);
                System.out.println("Total parcelado: " + totalInstallments);
                System.out.println("Total geral: " + grandTotal);
                System.out.println("Itens no mês: " + itemCount);
                System.out.println("Competencia: " + competency);
                saveReportUseCase.execute(null, user.get().getId(), competency, totalExpenses, totalInstallments, grandTotal, itemCount);
            }
        }

    }
}
