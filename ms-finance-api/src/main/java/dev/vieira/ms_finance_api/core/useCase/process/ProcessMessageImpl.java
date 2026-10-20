package dev.vieira.ms_finance_api.core.useCase.process;

import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramUpdateDto;
import dev.vieira.ms_finance_api.core.entities.Card;
import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.entities.User;
import dev.vieira.ms_finance_api.core.enums.ProcessConsumer;
import dev.vieira.ms_finance_api.core.gateway.ExpenseExporterGateway;
import dev.vieira.ms_finance_api.core.gateway.ParsedMessageGateway;
import dev.vieira.ms_finance_api.core.useCase.Card.CardUseCase;
import dev.vieira.ms_finance_api.core.useCase.expense.SaveExpenseUseCase;
import dev.vieira.ms_finance_api.core.useCase.report.contract.ProcessReportUseCase;
import dev.vieira.ms_finance_api.core.useCase.user.FindUserByChatIdUseCase;
import dev.vieira.ms_finance_api.core.useCase.user.SaveUserUseCase;
import dev.vieira.ms_finance_api.infrastructure.dto.Card.CreateCardDto;

public class ProcessMessageImpl implements ProcessMessageUseCase {

    private final FindUserByChatIdUseCase findUserByChatIdUseCase;
    private final SaveUserUseCase saveUserUseCase;
    private final SaveExpenseUseCase saveExpenseUseCase;
    private final ProcessReportUseCase processReportUseCase;
    private final ExpenseExporterGateway expenseExporterGateway;
    private final ParsedMessageGateway parsedMessageGateway;
    private final CardUseCase cardUseCase;

    public ProcessMessageImpl(SaveUserUseCase saveUserUseCase,
                              SaveExpenseUseCase saveExpenseUseCase,
                              ProcessReportUseCase processReportUseCase,
                              FindUserByChatIdUseCase findUserByChatIdUseCase,
                              ExpenseExporterGateway expenseExporterGateway,
                              ParsedMessageGateway parsedMessageGateway,
                              CardUseCase cardUseCase) {
        this.saveUserUseCase = saveUserUseCase;
        this.saveExpenseUseCase = saveExpenseUseCase;
        this.processReportUseCase = processReportUseCase;
        this.findUserByChatIdUseCase = findUserByChatIdUseCase;
        this.expenseExporterGateway = expenseExporterGateway;
        this.parsedMessageGateway = parsedMessageGateway;
        this.cardUseCase = cardUseCase;
    }


    @Override
    public void execute(TelegramUpdateDto payload, ProcessConsumer process) {
        System.out.println("[Core] Processando lógica de negócio da mensagem...");

        if(process.equals(ProcessConsumer.REPORT)) {
            System.out.println("Mensagem é um relatorio, direcionando para o ReportService");
            processReportUseCase.execute(payload);
            return;
        }

        var parsed = parsedMessageGateway.parse(payload.message().text());

        System.out.println("Gasto parseado: " + parsed);

        User user;
        Card card = null;

        var userCreated = findUserByChatIdUseCase.execute(
                payload.message().chat().id()
        );

        user = userCreated.orElseGet(() -> saveUserUseCase.execute(payload));

        if (parsed.cardName() != null && !parsed.cardName().isBlank()) {
            var cardCreated = cardUseCase.findCardByUserIdAndNameCard(
                    parsed.cardName(),
                    user.getId()
            );

            if (cardCreated.isPresent()) {
                System.out.println("findCardByUserIdAndNameCard está presente");
                card = cardCreated.get();
            } else {
                System.out.println("findCardByUserIdAndNameCard caiu no else");
                var createdCardDto = CreateCardDto.builder()
                        .userId(user.getId())
                        .name(parsed.cardName())
                        .build();

                card = cardUseCase.createCard(createdCardDto);
            }
        }

        Expense expense = saveExpenseUseCase.execute(
                user,
                payload,
                parsed,
                card
        );

        this.expenseExporterGateway.export(expense);
    }
}
