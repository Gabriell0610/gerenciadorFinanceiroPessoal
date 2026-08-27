package dev.vieira.ms_notification_api.service;

import dev.vieira.ms_notification_api.dto.TelegramChatDto;
import dev.vieira.ms_notification_api.dto.TelegramMessageDto;
import dev.vieira.ms_notification_api.dto.TelegramUpdateDto;
import dev.vieira.ms_notification_api.service.ExpenseService.ExpenseServiceImpl;
import dev.vieira.ms_notification_api.service.ProcessMessage.ProcessMessageImpl;
import dev.vieira.ms_notification_api.service.ReportService.ReportServiceImpl;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProcessMessageImplTest {

    @Mock
    private  Validator validator;
    @Mock
    private  ReportServiceImpl reportService;
    @Mock
    private  ExpenseServiceImpl expenseService;

    @InjectMocks
    private ProcessMessageImpl processMessage;

    private final String messageDto = "Gastei 123 no mercado";


    @Test
    void shouldProcessExpenseMessage() {

        var dto = mockTelegramDto(messageDto);

        processMessage.process(dto);

        verify(expenseService, times(1)).execute(
                eq(dto)
        );
        verify(reportService, never()).execute(any());

    }

    @Test
    void shouldProcessReportMessage() {

        var dto = mockTelegramDto("/relatorio");

        processMessage.process(dto);

        verify(reportService, times(1)).execute(dto);
        verify(expenseService, never()).execute(any());
    }

    private TelegramUpdateDto mockTelegramDto(String message) {

        var chatDto = new TelegramChatDto(
                1L,
                "Vieira",
                "private"
        );

        var messageDto = new TelegramMessageDto(
                1L,
                chatDto,
                message

        );

        return new TelegramUpdateDto(
                1L,
                messageDto
        );
    }

}
