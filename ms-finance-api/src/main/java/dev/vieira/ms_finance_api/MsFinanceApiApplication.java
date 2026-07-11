package dev.vieira.ms_finance_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "dev.vieira.ms_finance_api")
@SpringBootApplication
public class MsFinanceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsFinanceApiApplication.class, args);
	}

}
