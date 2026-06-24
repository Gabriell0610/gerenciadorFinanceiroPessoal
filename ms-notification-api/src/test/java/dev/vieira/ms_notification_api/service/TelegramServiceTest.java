package dev.vieira.ms_notification_api.service;

import dev.vieira.ms_notification_api.dto.TelegramChatDto;
import dev.vieira.ms_notification_api.dto.TelegramMessageDto;
import dev.vieira.ms_notification_api.dto.TelegramUpdateDto;
import dev.vieira.ms_notification_api.service.TelegramService.TelegramServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Set;

import static dev.vieira.ms_notification_api.config.RabbitMQConfig.EXCHANGE_TELEGRAM;
import static dev.vieira.ms_notification_api.config.RabbitMQConfig.ROUTING_KEY_TELEGRAM;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TelegramServiceTest {

    @Mock
    private Validator validator;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private TelegramServiceImpl telegramServiceImpl;


    @Test
    void shouldSendTelegramMessageToQueue() {

        //Arrange
        var dto = mockTelegramDto();

        //Act
        telegramServiceImpl.processTelegramMessage(dto);

        //Assert
        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(EXCHANGE_TELEGRAM),
                eq(ROUTING_KEY_TELEGRAM),
                eq(dto)
        );
    }

    @Test
    void shouldThrowExceptionWhenRabbitTemplateFailsToSend() {
        // Arrange
        var dto = mockTelegramDto();

        doThrow(new AmqpException("Broker unavailable"))
                .when(rabbitTemplate)
                .convertAndSend(
                        eq(EXCHANGE_TELEGRAM),
                        eq(ROUTING_KEY_TELEGRAM),
                        eq(dto)
                );

        // Act & Assert
        assertThrows(AmqpException.class,
                () -> telegramServiceImpl.processTelegramMessage(dto));
    }

    @Test
    void shouldNotSendMessageWhenValidationFails() {
        // Arrange
        var dto = mockTelegramDto();

        // 1. Cria um mock simples da violação
        ConstraintViolation<TelegramUpdateDto> violation = mock(ConstraintViolation.class);

        // Opcional: se o seu código usar o erro.getMessage(), você pode mocar o retorno dele aqui:
        when(violation.getMessage()).thenReturn("O texto da mensagem não pode ser vazio");

        // 2. Passa o mock para dentro do Set.of()
        when(validator.validate(dto)).thenReturn(Set.of(violation));

        // Act
        telegramServiceImpl.processTelegramMessage(dto);

        // Assert
        verify(rabbitTemplate, never()).convertAndSend(
                anyString(),
                anyString(),
                anyString()
        );
    }


    private TelegramUpdateDto mockTelegramDto() {
        var chatDto = new TelegramChatDto(
                1L,
                "Vieira",
                "private"
        );

        var messageDto = new TelegramMessageDto(
                1L,
                chatDto,
                "Hello, this is a test message!"

        );

        return new TelegramUpdateDto(
                1L,
                messageDto
        );
    }
}
