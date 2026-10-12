package dev.vieira.ms_finance_api.core.useCase.process;

import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramUpdateDto;
import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.entities.User;
import dev.vieira.ms_finance_api.core.enums.ProcessConsumer;
import dev.vieira.ms_finance_api.core.gateway.ExpenseExporterGateway;
import dev.vieira.ms_finance_api.core.gateway.ParsedMessageGateway;
import dev.vieira.ms_finance_api.core.useCase.expense.SaveExpenseUseCase;
import dev.vieira.ms_finance_api.core.useCase.report.contract.ProcessReportUseCase;
import dev.vieira.ms_finance_api.core.useCase.user.FindUserByChatIdUseCase;
import dev.vieira.ms_finance_api.core.useCase.user.SaveUserUseCase;

public class ProcessMessageImpl implements ProcessMessageUseCase {

    private final FindUserByChatIdUseCase findUserByChatIdUseCase;
    private final SaveUserUseCase saveUserUseCase;
    private final SaveExpenseUseCase saveExpenseUseCase;
    private final ProcessReportUseCase processReportUseCase;
    private final ExpenseExporterGateway expenseExporterGateway;
    private final ParsedMessageGateway parsedMessageGateway;

    public ProcessMessageImpl(SaveUserUseCase saveUserUseCase,
                              SaveExpenseUseCase saveExpenseUseCase,
                              ProcessReportUseCase processReportUseCase,
                              FindUserByChatIdUseCase findUserByChatIdUseCase,
                              ExpenseExporterGateway expenseExporterGateway,
                              ParsedMessageGateway parsedMessageGateway) {
        this.saveUserUseCase = saveUserUseCase;
        this.saveExpenseUseCase = saveExpenseUseCase;
        this.processReportUseCase = processReportUseCase;
        this.findUserByChatIdUseCase = findUserByChatIdUseCase;
        this.expenseExporterGateway = expenseExporterGateway;
        this.parsedMessageGateway = parsedMessageGateway;
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

        Expense expense;
        User user;
        var userCreated = findUserByChatIdUseCase.execute(payload.message().chat().id());

        if(userCreated.isPresent()) {
             expense = saveExpenseUseCase.execute(userCreated.get(), payload, parsed);
        }else {
            user  = saveUserUseCase.execute(payload);
            expense = saveExpenseUseCase.execute(user, payload, parsed);
        }

        System.out.println("Indo pro notion");
        //Enviar gasto para o notion
        this.expenseExporterGateway.export(expense);

    }
}
