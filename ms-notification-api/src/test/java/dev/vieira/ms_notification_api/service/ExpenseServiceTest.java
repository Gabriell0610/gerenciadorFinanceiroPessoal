package dev.vieira.ms_notification_api.service;

import dev.vieira.ms_notification_api.dto.TelegramChatDto;
import dev.vieira.ms_notification_api.dto.TelegramMessageDto;
import dev.vieira.ms_notification_api.dto.TelegramUpdateDto;
import dev.vieira.ms_notification_api.service.ExpenseService.ExpenseServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpException;

import java.util.Set;

import static dev.vieira.ms_notification_api.config.RabbitMQConfig.ROUTING_KEY_EXPENSE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {

    @Mock
    private Validator validator;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private ExpenseServiceImpl expenseServiceImpl;


    @Test
    void shouldSendTelegramMessageToQueue() {

        //Arrange
        var dto = mockTelegramDto();

        //Act
        expenseServiceImpl.execute(dto);

        //Assert
        verify(messageService, times(1)).sendMessage(
                eq(ROUTING_KEY_EXPENSE),
                eq(dto)
        );
    }

    @Test
    void shouldThrowExceptionWhenRabbitTemplateFailsToSend() {
        // Arrange
        var dto = mockTelegramDto();

        doThrow(new AmqpException("Broker unavailable"))
                .when(messageService)
                .sendMessage(
                        eq(ROUTING_KEY_EXPENSE),
                        eq(dto)
                );

        // Act & Assert
        assertThrows(AmqpException.class,
                () -> expenseServiceImpl.execute(dto));
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
