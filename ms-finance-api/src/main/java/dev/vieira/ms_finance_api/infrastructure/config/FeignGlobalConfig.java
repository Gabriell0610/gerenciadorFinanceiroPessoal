package dev.vieira.ms_finance_api.infrastructure.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class FeignGlobalConfig {

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(
                TimeUnit.SECONDS.toMillis(3),  // 3s inicial — falhas temporárias resolvem rápido
                TimeUnit.SECONDS.toMillis(3),  // 3s máximo — não penaliza muito o fluxo
                3      // 3 tentativas — razoável
        );
    }
}
