package dev.vieira.ms_finance_api.infrastructure.beans;


import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;
import dev.vieira.ms_finance_api.core.usecases.expense.FindAllReportsByUserIdImpl;
import dev.vieira.ms_finance_api.core.usecases.expense.FindAllReportsByUserIdUseCase;
import dev.vieira.ms_finance_api.core.usecases.expense.SaveExpenseImpl;
import dev.vieira.ms_finance_api.core.usecases.expense.SaveExpenseUseCase;
import dev.vieira.ms_finance_api.core.usecases.message.ProcessMessageImpl;
import dev.vieira.ms_finance_api.core.usecases.message.ProcessMessageUseCase;
import dev.vieira.ms_finance_api.core.usecases.report.*;
import dev.vieira.ms_finance_api.core.usecases.user.FindUserByChatIdImpl;
import dev.vieira.ms_finance_api.core.usecases.user.FindUserByChatIdUseCase;
import dev.vieira.ms_finance_api.core.usecases.user.SaveUserImpl;
import dev.vieira.ms_finance_api.core.usecases.user.SaveUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ProcessMessageUseCase processMessageTelegramUseCase(SaveUserUseCase saveUserUseCase, SaveExpenseUseCase saveExpenseUseCase, ProcessReportUseCase processReportUseCase, FindUserByChatIdUseCase findUserByChatIdUseCase) {
        return new ProcessMessageImpl(saveUserUseCase, saveExpenseUseCase, processReportUseCase, findUserByChatIdUseCase);
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
    public ProcessReportUseCase processReportUseCase(FindUserByChatIdUseCase findUserByChatIdUseCase, FindAllReportsByUserIdUseCase findAllReportsByUserIdUseCase,  SaveReportUseCase saveReportUseCase, FindReportByUserAndCompetencyUseCase findReportByUserAndCompetencyUseCase) {
        return new ProcessReportImpl(findUserByChatIdUseCase, findAllReportsByUserIdUseCase, saveReportUseCase, findReportByUserAndCompetencyUseCase);
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
    FindAllReportsByUserIdUseCase findAllReportsByUserIdUseCase(FinanceGateway financeGateway) {
        return new FindAllReportsByUserIdImpl(financeGateway);
    }

}
