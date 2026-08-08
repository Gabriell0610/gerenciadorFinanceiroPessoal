package dev.vieira.ms_notification_api.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class RabbitMQConfig {

    //NOTIFICATION-API PRODUZ ESSA FILA
    public static final String EXCHANGE = "telegram.exchange";
    public static final String QUEUE_EXPENSE = "expense.queue";
    public static final String ROUTING_KEY_EXPENSE = "expense";
    public static final String QUEUE_REPORT = "report.queue";
    public static final String ROUTING_KEY_REPORT = "report";

    //NOTIFICATION-API CONSOME ESSE FILA
    public static final String EXCHANGE_NOTIFICATION = "notification.exchange";
    public static final String ROUTING_KEY_NOTIFICATION_RESPONSE = "notification.response";
    public static final String QUEUE_NOTIFICATION_RESPONSE = "notification.response.queue";


    // 1. Declara a Exchange (Decide para qual fila a mensagem vai ser enviada)
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    // 2. Declara a Fila
    @Bean
    public Queue expenseQueue() {
        return new Queue(QUEUE_EXPENSE, true); // true = fila durável (não some se o RabbitMQ reiniciar)
    }

    @Bean
    public Queue reportQueue() {
        return new Queue(QUEUE_REPORT, true); // true = fila durável (não some se o RabbitMQ reiniciar)
    }

    // 3. Faz o vínculo (Binding) entre a Fila e a Exchange usando a Routing Key
    @Bean
    public Binding expenseBinding(Queue expenseQueue, DirectExchange exchange) {
        return BindingBuilder.bind(expenseQueue).to(exchange).with(ROUTING_KEY_EXPENSE);
    }

    @Bean
    public Binding reportBinding(Queue reportQueue, DirectExchange exchange) {
        return BindingBuilder.bind(reportQueue).to(exchange).with(ROUTING_KEY_REPORT);
    }

    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(EXCHANGE_NOTIFICATION);
    }

    @Bean
    public Queue notificationResponseQueue() {
        return new Queue(QUEUE_NOTIFICATION_RESPONSE, true);
    }

    @Bean
    public Binding notificationResponseBinding(Queue notificationResponseQueue) {
        return BindingBuilder
                .bind(notificationResponseQueue)
                .to(new DirectExchange(EXCHANGE_NOTIFICATION))
                .with(ROUTING_KEY_NOTIFICATION_RESPONSE);
    }

    // 4. CRUCIAL: Transforma os seus DTOs/Records automaticamente em JSON ao enviar para a fila
    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

}
