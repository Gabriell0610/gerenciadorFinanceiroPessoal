package dev.vieira.ms_finance_api.useCase.process;

import dev.vieira.ms_finance_api.core.dto.Expense.ParsedExpenseDto;
import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramChatDto;
import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramMessageDto;
import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramUpdateDto;
import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.entities.User;
import dev.vieira.ms_finance_api.core.enums.ProcessConsumer;
import dev.vieira.ms_finance_api.core.gateway.ExpenseExporterGateway;
import dev.vieira.ms_finance_api.core.useCase.expense.SaveExpenseUseCase;
import dev.vieira.ms_finance_api.core.useCase.process.ProcessMessageImpl;
import dev.vieira.ms_finance_api.core.useCase.report.contract.ProcessReportUseCase;
import dev.vieira.ms_finance_api.core.useCase.user.FindUserByChatIdUseCase;
import dev.vieira.ms_finance_api.core.useCase.user.SaveUserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProcessMessageImplTest {

    @Mock
    private  FindUserByChatIdUseCase findUserByChatIdUseCase;
    @Mock
    private  SaveUserUseCase saveUserUseCase;
    @Mock
    private  SaveExpenseUseCase saveExpenseUseCase;
    @Mock
    private  ProcessReportUseCase processReportUseCase;
    @Mock
    private  ExpenseExporterGateway expenseExporterGateway;


    @InjectMocks
    private ProcessMessageImpl processMessageImpl;


    @Test
    void shouldSendForUseCaseReport() {

        //Arrange
        var payload = createPayloadTelegram("/relatorio");
        var process = ProcessConsumer.REPORT;

        //Act
        processMessageImpl.execute(payload, process);

        //Assert
        verify(processReportUseCase, times(1)).execute(payload);


    }

    @Test
    void ShouldntParseCorrectlyWithEmojiMessage() {

        //Arrange
        var payload = createPayloadTelegram("😀");
        var process = ProcessConsumer.EXPENSE;


        //Act
        processMessageImpl.execute(payload, process);

        //Assert
        verify(findUserByChatIdUseCase, times(0)).execute(payload.message().chat().id());
        verify(saveExpenseUseCase, never()).execute(any(), any(), any());
        verify(saveUserUseCase, times(0)).execute(payload);
        verify(expenseExporterGateway, times(0)).export(null);

    }

    @Test
    void ShouldntParsedWithOnlyNumberMessage() {

        //Arrange
        var payload = createPayloadTelegram("32");
        var process = ProcessConsumer.EXPENSE;

        //Act
        processMessageImpl.execute(payload, process);

        //Assert
        verify(findUserByChatIdUseCase, times(0)).execute(payload.message().chat().id());
        verify(saveExpenseUseCase, never()).execute(any(), any(), any());
        verify(saveUserUseCase, times(0)).execute(payload);
        verify(expenseExporterGateway, times(0)).export(null);

    }

    @Test
    void ShouldParseCorrectlyWithNumberAndInstallment() {

        //Arrange
        var payload = createPayloadTelegram("PS5 2500 10");
        var process = ProcessConsumer.EXPENSE;
        var parsed = createParsedExapanseDto("PS5", "Outros", BigDecimal.valueOf(2500), 10);


        User user = new User(
                "Gabriel",
                "gb@gmail.com",
                payload.message().chat().id()
        );

        Expense expense = new Expense(
                user.getId(),
                parsed.valor(),
                payload.message().text(),
                parsed.nome(),
                parsed.parcelas(),
                parsed.category()
        );

        when(findUserByChatIdUseCase.execute(payload.message().chat().id())).thenReturn(java.util.Optional.of(user));
        when(saveExpenseUseCase.execute(user, payload, parsed)).thenReturn(expense);

        //Act
        processMessageImpl.execute(payload, process);

        //Assert
        verify(findUserByChatIdUseCase, times(1)).execute(payload.message().chat().id());
        verify(saveExpenseUseCase, times(1)).execute(user, payload, parsed);
        verify(saveUserUseCase, times(0)).execute(payload);
        verify(expenseExporterGateway, times(1)).export(expense);

        assertEquals(expense.getUserId(), user.getId());
        assertEquals(expense.getAmount(), parsed.valor());
        assertEquals(expense.getDescription(), parsed.nome());

    }

    @Test
    void ShouldFindUserWithSucessAndSaveExpense() {

        //Arrange
        var payload = createPayloadTelegram("Uber 32");
        var process = ProcessConsumer.EXPENSE;
        var parsed = createParsedExapanseDto("Uber", "Transporte", BigDecimal.valueOf(32), 1);


        User user = new User(
                "Gabriel",
                "gb@gmail.com",
                payload.message().chat().id()
        );

        Expense expense = new Expense(
                user.getId(),
                parsed.valor(),
                payload.message().text(),
                parsed.nome(),
                parsed.parcelas(),
                parsed.category()
        );

        when(findUserByChatIdUseCase.execute(payload.message().chat().id())).thenReturn(java.util.Optional.of(user));
        when(saveExpenseUseCase.execute(user, payload, parsed)).thenReturn(expense);

        //Act
        processMessageImpl.execute(payload, process);

        //Assert
        verify(findUserByChatIdUseCase, times(1)).execute(payload.message().chat().id());
        verify(saveExpenseUseCase, times(1)).execute(user, payload, parsed);
        verify(saveUserUseCase, times(0)).execute(payload);
        verify(expenseExporterGateway, times(1)).export(expense);

        assertEquals(expense.getUserId(), user.getId());
        assertEquals(expense.getAmount(), parsed.valor());
        assertEquals(expense.getDescription(), parsed.nome());

    }

    @Test
    void ShouldSaveExpanseWithANewUser() {
        var payload = createPayloadTelegram("Uber 32");
        var process = ProcessConsumer.EXPENSE;
        var parsed = createParsedExapanseDto("Uber", "Transporte", BigDecimal.valueOf(32), 1);


        User user = new User(
                "Pedro",
                "pedro@gmail.com",
                payload.message().chat().id()
        );

        Expense expense = new Expense(
                user.getId(),
                parsed.valor(),
                payload.message().text(),
                parsed.nome(),
                parsed.parcelas(),
                parsed.category()
        );

        when(findUserByChatIdUseCase.execute(payload.message().chat().id())).thenReturn(java.util.Optional.empty());
        when(saveExpenseUseCase.execute(user, payload, parsed)).thenReturn(expense);
        when(saveUserUseCase.execute(payload)).thenReturn(user);

        //Act
        processMessageImpl.execute(payload, process);

        //Assert
        verify(findUserByChatIdUseCase, times(1)).execute(payload.message().chat().id());
        verify(saveExpenseUseCase, times(1)).execute(user, payload, parsed);
        verify(saveUserUseCase, times(1)).execute(payload);
        verify(expenseExporterGateway, times(1)).export(expense);

        assertEquals(expense.getUserId(), user.getId());
        assertEquals(expense.getAmount(), parsed.valor());
        assertEquals(expense.getDescription(), parsed.nome());
    }


    private TelegramUpdateDto createPayloadTelegram(String message) {
        var chatDto = new TelegramChatDto(
                1L,
                "gabriel",
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

    private ParsedExpenseDto createParsedExapanseDto(String name, String category, BigDecimal amount, int installment) {
        return new ParsedExpenseDto(
                name,
                category,
                amount,
                installment
        );

    }


}
