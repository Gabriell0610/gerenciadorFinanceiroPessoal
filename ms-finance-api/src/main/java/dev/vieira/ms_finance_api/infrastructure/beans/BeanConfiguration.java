package dev.vieira.ms_finance_api.infrastructure.beans;


import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;
import dev.vieira.ms_finance_api.core.usecases.ProcessMessageImpl;
import dev.vieira.ms_finance_api.core.usecases.ProcessMessageUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ProcessMessageUseCase processMessageTelegramUseCase(FinanceGateway financeGateway) {
        return new ProcessMessageImpl(financeGateway);
    }

}
