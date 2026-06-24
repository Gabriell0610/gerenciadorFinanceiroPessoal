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

    public static final String QUEUE_TELEGRAM = "telegram.messages.queue";
    public static final String EXCHANGE_TELEGRAM = "telegram.messages.exchange";
    public static final String ROUTING_KEY_TELEGRAM = "telegram.messages.routing.key";

    // 1. Declara a Fila
    @Bean
    public Queue telegramQueue() {
        return new Queue(QUEUE_TELEGRAM, true); // true = fila durável (não some se o RabbitMQ reiniciar)
    }

    // 2. Declara a Exchange (Direct é perfeita para esse cenário de 1 para 1)
    @Bean
    public DirectExchange telegramExchange() {
        return new DirectExchange(EXCHANGE_TELEGRAM);
    }

    // 3. Faz o vínculo (Binding) entre a Fila e a Exchange usando a Routing Key
    @Bean
    public Binding telegramBinding(Queue telegramQueue, DirectExchange telegramExchange) {
        return BindingBuilder.bind(telegramQueue).to(telegramExchange).with(ROUTING_KEY_TELEGRAM);
    }

    // 4. CRUCIAL: Transforma os seus DTOs/Records automaticamente em JSON ao enviar para a fila
    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
