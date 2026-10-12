package dev.vieira.ms_finance_api.infrastructure.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //FINANCE-API CONSOME ESSAS DUAS FILAS
    public static final String EXCHANGE_TELEGRAM = "telegram.exchange";
    public static final String QUEUE_EXPENSE = "expense.queue";
    public static final String QUEUE_REPORT = "report.queue";
    public static final String ROUTING_KEY_REPORT = "report";
    public static final String ROUTING_KEY_EXPENSE = "expense";

    //FINANCE-API PRODUZ ESSA FILA
    public static final String EXCHANGE = "notification.exchange";
    public static final String QUEUE_NOTIFICATION_RESPONSE = "notification.response.queue";
    public static final String ROUTING_KEY_NOTIFICATION_RESPONSE = "notification.response";

    //DQL
    public static final String QUEUE_NOTION_DEAD = "notion.dead.queue";
    public static final String ROUTING_KEY_NOTION_DEAD = "notion.dead";


    @Bean
    public Queue notionDeadQueue() {
        return QueueBuilder.durable(QUEUE_NOTION_DEAD).build();
    }

    @Bean
    public Binding notionDeadBinding(Queue notionDeadQueue) {
        return BindingBuilder
                .bind(notionDeadQueue)
                .to(new DirectExchange(EXCHANGE))
                .with(ROUTING_KEY_NOTION_DEAD);
    }


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
        return BindingBuilder.bind(notificationQueue()).to(exchange).with(ROUTING_KEY_NOTIFICATION_RESPONSE);
    }

    @Bean
    public DirectExchange telegramExchange() {
        return new DirectExchange(EXCHANGE_TELEGRAM);
    }

    @Bean
    public Queue expenseQueue() {
        return new Queue(QUEUE_EXPENSE, true);
    }

    @Bean
    public Queue reportQueue() {
        return new Queue(QUEUE_REPORT, true);
    }

    @Bean
    public Binding expenseBinding(Queue expenseQueue) {
        return BindingBuilder
                .bind(expenseQueue)
                .to(new DirectExchange(EXCHANGE_TELEGRAM))
                .with(ROUTING_KEY_EXPENSE);
    }

    @Bean
    public Binding reportBinding(Queue reportQueue) {
        return BindingBuilder
                .bind(reportQueue)
                .to(new DirectExchange(EXCHANGE_TELEGRAM))
                .with(ROUTING_KEY_REPORT);
    }


    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
