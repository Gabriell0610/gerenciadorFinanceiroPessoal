package dev.vieira.ms_finance_api.core.useCase.report;

import dev.vieira.ms_finance_api.core.dto.Report.NotificationResponseDto;
import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramUpdateDto;
import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.entities.Report;
import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;
import dev.vieira.ms_finance_api.core.useCase.expense.FindAllExpenseByUserIdUseCase;
import dev.vieira.ms_finance_api.core.useCase.user.FindUserByChatIdUseCase;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class ProcessReportImpl implements  ProcessReportUseCase{

    private final FindUserByChatIdUseCase findUserByChatIdUseCase;
    private final FindAllExpenseByUserIdUseCase findAllExpenseByUserIdUseCase;
    private final FindReportByUserAndCompetencyUseCase findReportByUserAndCompetencyUseCase;
    private final SaveReportUseCase saveReportUseCase;
    private final SendNotificationUseCase sendNotificationUseCase;
    private final ReportArchiveUseCase reportArchiveUseCase;
    private final FinanceGateway financeGateway;

    public ProcessReportImpl(FindUserByChatIdUseCase findUserByChatIdUseCase,
                             FindAllExpenseByUserIdUseCase findAllReportsByUserIdUseCase,
                             SaveReportUseCase saveReportUseCase,
                             FindReportByUserAndCompetencyUseCase findReportByUserAndCompetencyUseCase,
                             SendNotificationUseCase sendNotificationUseCase,
                             ReportArchiveUseCase reportArchiveUseCase,
                             FinanceGateway financeGateway) {
        this.findUserByChatIdUseCase = findUserByChatIdUseCase;
        this.findAllExpenseByUserIdUseCase = findAllReportsByUserIdUseCase;
        this.saveReportUseCase = saveReportUseCase;
        this.findReportByUserAndCompetencyUseCase = findReportByUserAndCompetencyUseCase;
        this.sendNotificationUseCase = sendNotificationUseCase;
        this.reportArchiveUseCase = reportArchiveUseCase;
        this.financeGateway = financeGateway;
    }

    @Override
    public void execute(TelegramUpdateDto payload) {
        System.out.println("Processando relatório para usuario");

        var user = findUserByChatIdUseCase.execute(payload.message().chat().id());

        if(user.isEmpty()) return; // lançar exceção aqui;

        System.out.println("Usuario encontrado: " + user.get().getName());
        System.out.println("Gerando relatório para o usuario: " + user.get().getName());

        LocalDate competency = LocalDate.now(ZoneId.of("America/Sao_Paulo"))
                .withDayOfMonth(1);

        var allExpenses = this.findAllExpenseByUserIdUseCase.execute(user.get().getId());
        System.out.println("Gastos do usuário:  " + allExpenses);

        //filtra só os que aparecem no mês da competência
        var monthlyExpenses = filterMonthlyExpenses(allExpenses, competency);
        System.out.println("Gastos do usuário no mês da competência:  " + monthlyExpenses);

        BigDecimal totalExpenses = calculateExpenseByInstallment(monthlyExpenses, 1);
        BigDecimal totalInstallments = calculateExpenseByInstallment(monthlyExpenses, 2);
        BigDecimal grandTotal = totalExpenses.add(totalInstallments);
        int itemCount = monthlyExpenses.size();


        // Upsert do relário
        var existingReport = findReportByUserAndCompetencyUseCase.execute(user.get().getId(), competency);
        UUID reportId = existingReport.map(Report::getId).orElse(null);

        var report = saveReportUseCase.execute(reportId, user.get().getId(), competency, totalExpenses, totalInstallments, grandTotal, itemCount);

        var formatMessage = this.formatMessage(report, competency);

        NotificationResponseDto notificationResponseDto = new NotificationResponseDto(
                user.get().getChatId(),
                formatMessage
        );

        sendNotificationUseCase.execute(notificationResponseDto);

//        //byte[] csv = reportArchiveUseCase.process(report, monthlyExpenses);
//
//        //financeGateway.sendReportFile(csv, payload.message().chat().id());
//
//        Files.write(Path.of("relatorio.csv"), csv);
//        System.out.println("CSV gerado em: " + Path.of("relatorio.csv").toAbsolutePath());
//        System.out.println(new String(csv, StandardCharsets.UTF_8));
    }

    private List<Expense> filterMonthlyExpenses(List<Expense> expenses, LocalDate competency) {
        return expenses.stream()
                .filter(expense -> {
                    long mesesPassados = Period.between(
                            expense.getCreated_at().toLocalDate().withDayOfMonth(1),
                            competency
                    ).toTotalMonths();
                    return mesesPassados >= 0 && mesesPassados < expense.getInstallment();
                })
                .toList();
    }

    private BigDecimal calculateExpenseByInstallment(List<Expense> expenses, int minInstallment) {
        return expenses.stream()
                .filter(e -> minInstallment == 1 ? e.getInstallment() == 1 : e.getInstallment() > 1)
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    private String formatMessage (Report report, LocalDate competency) {
        String date = competency.format(DateTimeFormatter.ofPattern("MM/yyyy"));

        return String.format("""
        📊 <b>Relatório %s</b>
        
        🛒 Gastos simples: R$ %.2f
        💳 Parcelados: R$ %.2f
        💰 Total geral: R$ %.2f
        📦 Itens no mês: %d
        
        """,
                date,
                report.getTotalExpenses(),
                report.getTotalInstallments(),
                report.getGrandTotal(),
                report.getItemCount()
        );

    }
}
