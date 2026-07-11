package dev.vieira.ms_notification_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "dev.vieira.ms_notification_api.resource.client")
@SpringBootApplication
public class MsNotificationApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsNotificationApiApplication.class, args);
	}

}
