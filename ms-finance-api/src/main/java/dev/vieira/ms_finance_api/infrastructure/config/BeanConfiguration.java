package dev.vieira.ms_finance_api.infrastructure.config;


import dev.vieira.ms_finance_api.core.gateway.ExpenseExporterGateway;
import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;
import dev.vieira.ms_finance_api.core.gateway.ParsedMessageGateway;
import dev.vieira.ms_finance_api.core.useCase.Card.CardImpl;
import dev.vieira.ms_finance_api.core.useCase.Card.CardUseCase;
import dev.vieira.ms_finance_api.core.useCase.expense.*;
import dev.vieira.ms_finance_api.core.useCase.process.ProcessMessageImpl;
import dev.vieira.ms_finance_api.core.useCase.process.ProcessMessageUseCase;
import dev.vieira.ms_finance_api.core.useCase.report.contract.*;
import dev.vieira.ms_finance_api.core.useCase.report.impl.*;
import dev.vieira.ms_finance_api.core.useCase.user.FindUserByChatIdImpl;
import dev.vieira.ms_finance_api.core.useCase.user.FindUserByChatIdUseCase;
import dev.vieira.ms_finance_api.core.useCase.user.SaveUserImpl;
import dev.vieira.ms_finance_api.core.useCase.user.SaveUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ProcessMessageUseCase processMessageTelegramUseCase(SaveUserUseCase saveUserUseCase, SaveExpenseUseCase saveExpenseUseCase,
                                                               ProcessReportUseCase processReportUseCase,
                                                               FindUserByChatIdUseCase findUserByChatIdUseCase,
                                                               ExpenseExporterGateway expenseExporterGateway,
                                                               ParsedMessageGateway parsedMessageGateway,
                                                               CardUseCase cardUseCase) {
        return new ProcessMessageImpl(saveUserUseCase, saveExpenseUseCase,
                processReportUseCase,
                findUserByChatIdUseCase,
                expenseExporterGateway,
                parsedMessageGateway,
                cardUseCase);
    }

    @Bean
    public SaveExpenseUseCase saveExpenseUseCase(FinanceGateway financeGateway) {
        return new SaveExpenseImpl(financeGateway);
    }

    @Bean
    public SaveUserUseCase saveUserUseCase(FinanceGateway financeGateway) {
        return new SaveUserImpl(financeGateway);
    }

    @Bean
    public ProcessReportUseCase processReportUseCase(FindUserByChatIdUseCase findUserByChatIdUseCase,
                                                     FindAllExpenseByUserIdWithCompetencyUseCase findAllExpenseByUserIdWithCompetencyUseCase,
                                                     SaveReportUseCase saveReportUseCase,
                                                     FindReportByUserAndCompetencyUseCase findReportByUserAndCompetencyUseCase,
                                                     SendNotificationUseCase sendNotificationUseCase,
                                                     ReportArchiveUseCase reportArchiveUseCase,
                                                     FinanceGateway financeGateway) {
        return new ProcessReportImpl(findUserByChatIdUseCase,
                findAllExpenseByUserIdWithCompetencyUseCase,
                saveReportUseCase,
                findReportByUserAndCompetencyUseCase,sendNotificationUseCase, reportArchiveUseCase, financeGateway);
    }

    @Bean
    public ReportArchiveUseCase reportArchiveUseCase() {
        return new ReportArchiveImpl();
    }

    @Bean
    public FindReportByUserAndCompetencyUseCase findReportByUserAndCompetencyUseCase(FinanceGateway financeGateway) {
        return new FindReportByUserAndCompetencyImpl(financeGateway);

    }

    @Bean
    public SaveReportUseCase saveReportUseCase(FinanceGateway financeGateway) {
        return new SaveReportImpl(financeGateway);
    }

    @Bean
    public FindUserByChatIdUseCase findUserByChatIdUseCase(FinanceGateway financeGateway) {
        return new FindUserByChatIdImpl(financeGateway);
    }

    @Bean
    FindAllExpenseByUserIdWithCompetencyUseCase findAllExpenseByUserIdWithCompetencyUseCase(FinanceGateway financeGateway) {
        return new FindAllExpenseByUserIdWithCompetencyImpl(financeGateway);
    }

    @Bean SendNotificationUseCase sendNotificationUseCase(FinanceGateway financeGateway) {
        return new SendNotification(financeGateway);
    }

    @Bean CardUseCase cardUseCase(FinanceGateway financeGateway) {
        return new CardImpl(financeGateway);
    }

}
