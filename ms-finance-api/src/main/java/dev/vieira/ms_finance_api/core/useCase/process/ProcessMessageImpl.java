package dev.vieira.ms_finance_api.core.useCase.process;

import dev.vieira.ms_finance_api.core.dto.Expense.ParsedExpenseDto;
import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramUpdateDto;
import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.entities.User;
import dev.vieira.ms_finance_api.core.enums.ProcessConsumer;
import dev.vieira.ms_finance_api.core.gateway.ExpenseExporterGateway;
import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;
import dev.vieira.ms_finance_api.core.gateway.PromptFinanceGatewayImpl;
import dev.vieira.ms_finance_api.core.useCase.expense.SaveExpenseUseCase;
import dev.vieira.ms_finance_api.core.useCase.report.contract.ProcessReportUseCase;
import dev.vieira.ms_finance_api.core.useCase.user.FindUserByChatIdUseCase;
import dev.vieira.ms_finance_api.core.useCase.user.SaveUserUseCase;
import dev.vieira.ms_finance_api.infrastructure.dto.GeminiDto.GeminiResponseDto;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.*;

public class ProcessMessageImpl implements ProcessMessageUseCase {

    private final FindUserByChatIdUseCase findUserByChatIdUseCase;
    private final SaveUserUseCase saveUserUseCase;
    private final SaveExpenseUseCase saveExpenseUseCase;
    private final ProcessReportUseCase processReportUseCase;
    private final ExpenseExporterGateway expenseExporterGateway;
    private final ObjectMapper objectMapper;
    private final PromptFinanceGatewayImpl promptFinanceGateway;

    public ProcessMessageImpl(SaveUserUseCase saveUserUseCase,
                              SaveExpenseUseCase saveExpenseUseCase,
                              ProcessReportUseCase processReportUseCase,
                              FindUserByChatIdUseCase findUserByChatIdUseCase,
                              ExpenseExporterGateway expenseExporterGateway,
                              ObjectMapper objectMapper,
                              PromptFinanceGatewayImpl promptFinanceGateway) {
        this.saveUserUseCase = saveUserUseCase;
        this.saveExpenseUseCase = saveExpenseUseCase;
        this.processReportUseCase = processReportUseCase;
        this.findUserByChatIdUseCase = findUserByChatIdUseCase;
        this.expenseExporterGateway = expenseExporterGateway;
        this.objectMapper = objectMapper;
        this.promptFinanceGateway = promptFinanceGateway;
    }


    @Override
    public void execute(TelegramUpdateDto payload, ProcessConsumer process) {
        System.out.println("[Core] Processando lógica de negócio da mensagem...");
        System.out.println("teste deploy"); // tirar, é a penas teste

        if(process.equals(ProcessConsumer.REPORT)) {
            System.out.println("Mensagem é um relatorio, direcionando para o ReportService");
            processReportUseCase.execute(payload);
            return;
        }

        GeminiResponseDto geminiResponse  = this.promptFinanceGateway.processMessage(payload.message().text());

        String json = geminiResponse.candidates()
                .get(0)
                .content()
                .parts()
                .get(0)
                .text();

        objectMapper.registeredModules();
        ParsedExpenseDto parsed = objectMapper.readValue(json, ParsedExpenseDto.class);

        if (parsed.description().isEmpty()) {
            System.out.println("Mensagem inválida, não foi possível parsear o gasto.");
            return;
        }

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

        //Enviar gasto para o notion
        this.expenseExporterGateway.export(expense);

    }
}
