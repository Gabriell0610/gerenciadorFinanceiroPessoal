package dev.vieira.ms_finance_api.infrastructure.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_EXPENSE = "expense.queue";
    public static final String QUEUE_REPORT = "report.queue";

    public static final String EXCHANGE = "notification.exchange";
    public static final String QUEUE_NOTIFICATION_RESPONSE = "notification.response.queue";
    public static final String ROUTING_KEY_NOTIFICATION_RESPONSE = "notification.response";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    // 2. Declara a Fila
    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE_NOTIFICATION_RESPONSE, true); // true = fila durável (não some se o RabbitMQ reiniciar)
    }

    // 3. Faz o vínculo (Binding) entre a Fila e a Exchange usando a Routing Key
    @Bean
    public Binding notificationBinding(Queue reportQueue, DirectExchange exchange) {
        return BindingBuilder.bind(reportQueue).to(exchange).with(ROUTING_KEY_NOTIFICATION_RESPONSE);
    }


    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
