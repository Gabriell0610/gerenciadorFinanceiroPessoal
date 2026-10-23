package dev.vieira.ms_finance_api.core.useCase.process;

import dev.vieira.ms_finance_api.core.dto.Expense.ParsedExpenseDto;
import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramChatDto;
import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramMessageDto;
import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramUpdateDto;
import dev.vieira.ms_finance_api.core.entities.Card;
import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.entities.User;
import dev.vieira.ms_finance_api.core.enums.CardExpirationDate;
import dev.vieira.ms_finance_api.core.enums.ProcessConsumer;
import dev.vieira.ms_finance_api.core.gateway.ExpenseExporterGateway;
import dev.vieira.ms_finance_api.core.gateway.ParsedMessageGateway;
import dev.vieira.ms_finance_api.core.gateway.PromptFinanceGateway;
import dev.vieira.ms_finance_api.core.useCase.Card.CardUseCase;
import dev.vieira.ms_finance_api.core.useCase.expense.SaveExpenseUseCase;
import dev.vieira.ms_finance_api.core.useCase.report.contract.ProcessReportUseCase;
import dev.vieira.ms_finance_api.core.useCase.user.FindUserByChatIdUseCase;
import dev.vieira.ms_finance_api.core.useCase.user.SaveUserUseCase;
import dev.vieira.ms_finance_api.infrastructure.dto.GeminiDto.GeminiResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

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
    private ParsedMessageGateway parsedMessageGateway;
    @Mock
    private CardUseCase cardUseCase;

    private User user;
    private Card card;

    @InjectMocks
    ProcessMessageImpl processMessage;

    @BeforeEach
    void setUp() {
        // Executado ANTES de cada teste
        // Útil para configurar comportamentos padrão ou limpar dados

        user = new User(
                "joao",
                "jv@gmail.com",
                1L
        );

        card = new Card(
                user.getId(),
                "picpay",
                CardExpirationDate.VENCIMENTO_CINCO,
                BigDecimal.ZERO

        );
    }

    @Test
    void shouldProcessReportMessage() {

        var dto = mockTelegramDto("/relatorio");

        processMessage.execute(dto, ProcessConsumer.REPORT);

        verify(processReportUseCase,times(1)).execute(dto);
    }

    @Test
    void shouldProcessExpenseMessageWithCreatedUser() {

        var dto = mockTelegramDto(messageDto);
        String json = "json-fake";

        var geminiDto = new GeminiResponseDto(
                List.of(new GeminiResponseDto.Candidates(
                        new GeminiResponseDto.Content(
                                List.of(new GeminiResponseDto.Parts(json))
                        )
                ))
        );

        Expense expense = new Expense(
            user.getId(),
            card.getId(),
            BigDecimal.valueOf(123),
            messageDto,
            "mercado",
            1,
            "Mercado",
            LocalDate.now()
        );

        ParsedExpenseDto parsed = new ParsedExpenseDto(
                "mercado",
                BigDecimal.valueOf(123),
                "Mercado",
                1,
                LocalDate.now(),
                "picpay"
        );

        doReturn(parsed).when(parsedMessageGateway).parse(dto.message().text());
        doReturn(Optional.of(user)).when(findUserByChatIdUseCase).execute(dto.message().chat().id());
        doReturn(Optional.of(card)).when(cardUseCase).findCardByUserIdAndNameCard(parsed.cardName(), user.getId());
        doReturn(expense).when(saveExpenseUseCase).execute(user, dto, parsed, card);

        processMessage.execute(dto, ProcessConsumer.EXPENSE);

        verify(saveExpenseUseCase, times(1)).execute(user,dto, parsed, card);
        verify(processReportUseCase,times(0)).execute(dto);
        verify(saveUserUseCase,times(0)).execute(dto);
        verify(expenseExporterGateway, times(1)).export(expense);
    }

    @Test
    void shouldProcessExpenseMessageWithNewUser() {
        var dto = mockTelegramDto(messageDto);
        String json = "json-fake";

        var geminiDto = new GeminiResponseDto(
                List.of(new GeminiResponseDto.Candidates(
                        new GeminiResponseDto.Content(
                                List.of(new GeminiResponseDto.Parts(json))
                        )
                ))
        );

        Expense expense = new Expense(
                user.getId(),
                card.getId(),
                BigDecimal.valueOf(123),
                messageDto,
                "mercado",
                1,
                "Mercado",
                LocalDate.now()
        );

        ParsedExpenseDto parsed = new ParsedExpenseDto(
                "mercado",
                BigDecimal.valueOf(123),
                "Mercado",
                1,
                LocalDate.now(),
                "picpay"
        );

        doReturn(parsed).when(parsedMessageGateway).parse(dto.message().text());
        doReturn(Optional.empty()).when(findUserByChatIdUseCase).execute(dto.message().chat().id());
        doReturn(Optional.of(card)).when(cardUseCase).findCardByUserIdAndNameCard(parsed.cardName(), user.getId());
        doReturn(user).when(saveUserUseCase).execute(dto);
        doReturn(expense).when(saveExpenseUseCase).execute(user, dto, parsed, card);

        processMessage.execute(dto, ProcessConsumer.EXPENSE);

        verify(saveExpenseUseCase, times(1)).execute(user,dto, parsed, card);
        verify(processReportUseCase,times(0)).execute(dto);
        verify(expenseExporterGateway, times(1)).export(expense);
    }

//    @Test
//    void shouldntProcessExpenseWithEmptyMessage() {
//        var dto = mockTelegramDto(messageDto);
//        String json = "json-fake";
//
//        var geminiDto = new GeminiResponseDto(
//                List.of(new GeminiResponseDto.Candidates(
//                        new GeminiResponseDto.Content(
//                                List.of(new GeminiResponseDto.Parts(json))
//                        )
//                ))
//        );
//
//        ParsedExpenseDto parsed = new ParsedExpenseDto(
//                "",
//                BigDecimal.valueOf(123),
//                "Mercado",
//                1,
//                LocalDate.now()
//        );
//
//        doReturn(parsed).when(parsedMessageGateway).parse(dto.message().text());
//
//        processMessage.execute(dto, ProcessConsumer.EXPENSE);
//
//        verify(parsedMessageGateway, times(1)).parse(dto.message().text());
//        verify(processReportUseCase,times(0)).execute(dto);
//        verifyNoInteractions(findUserByChatIdUseCase, saveUserUseCase, saveExpenseUseCase, expenseExporterGateway);
//    }



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
