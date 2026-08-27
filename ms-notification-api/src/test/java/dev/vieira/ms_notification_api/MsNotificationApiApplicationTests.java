package dev.vieira.ms_notification_api;

import dev.vieira.ms_notification_api.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@TestPropertySource(properties = {
		"telegram_token=fake-token",
		"spring.rabbitmq.addresses=amqp://guest:guest@localhost:5672"
})
class MsNotificationApiApplicationTests {

	@MockitoBean
	MessageService messageService;

	@Test
	void contextLoads() {
	}

}
