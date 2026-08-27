package dev.vieira.ms_finance_api.core.useCase.process;

import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramChatDto;
import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramMessageDto;
import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramUpdateDto;
import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.enums.ProcessConsumer;
import dev.vieira.ms_finance_api.core.gateway.ExpenseExporterGateway;
import dev.vieira.ms_finance_api.core.gateway.PromptFinanceGatewayImpl;
import dev.vieira.ms_finance_api.core.useCase.expense.SaveExpenseUseCase;
import dev.vieira.ms_finance_api.core.useCase.report.contract.ProcessReportUseCase;
import dev.vieira.ms_finance_api.core.useCase.user.FindUserByChatIdUseCase;
import dev.vieira.ms_finance_api.core.useCase.user.SaveUserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProcessMessageTest {

    private final String messageDto = "Gastei 123 no mercado";

    @Mock
    private FindUserByChatIdUseCase findUserByChatIdUseCase;
    @Mock
    private SaveUserUseCase saveUserUseCase;
    @Mock
    private SaveExpenseUseCase saveExpenseUseCase;
    @Mock
    private ProcessReportUseCase processReportUseCase;
    @Mock
    private ExpenseExporterGateway expenseExporterGateway;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private PromptFinanceGatewayImpl promptFinanceGateway;

    @InjectMocks
    ProcessMessageImpl processMessage;

    @Test
    void shouldProcessReportMessage() {

        var dto = mockTelegramDto("/relatorio");

        processMessage.execute(dto, ProcessConsumer.REPORT);

        verify(processReportUseCase,times(1)).execute(dto);
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
