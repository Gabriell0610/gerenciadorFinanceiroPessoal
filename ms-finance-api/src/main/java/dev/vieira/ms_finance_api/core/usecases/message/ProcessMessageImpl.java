package dev.vieira.ms_finance_api.core.usecases.message;

import dev.vieira.ms_finance_api.core.dto.Expense.ParsedExpenseDto;
import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramUpdateDto;
import dev.vieira.ms_finance_api.core.entities.User;
import dev.vieira.ms_finance_api.core.enums.ProcessConsumer;
import dev.vieira.ms_finance_api.core.usecases.expense.SaveExpenseUseCase;
import dev.vieira.ms_finance_api.core.usecases.report.ProcessReportUseCase;
import dev.vieira.ms_finance_api.core.usecases.user.FindUserByChatIdImpl;
import dev.vieira.ms_finance_api.core.usecases.user.FindUserByChatIdUseCase;
import dev.vieira.ms_finance_api.core.usecases.user.SaveUserUseCase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProcessMessageImpl implements ProcessMessageUseCase {

    private final FindUserByChatIdUseCase findUserByChatIdUseCase;
    private final SaveUserUseCase saveUserUseCase;
    private final SaveExpenseUseCase saveExpenseUseCase;
    private final ProcessReportUseCase processReportUseCase;


    public ProcessMessageImpl(SaveUserUseCase saveUserUseCase, SaveExpenseUseCase saveExpenseUseCase, ProcessReportUseCase processReportUseCase, FindUserByChatIdUseCase findUserByChatIdUseCase) {
        this.saveUserUseCase = saveUserUseCase;
        this.saveExpenseUseCase = saveExpenseUseCase;
        this.processReportUseCase = processReportUseCase;
        this.findUserByChatIdUseCase = findUserByChatIdUseCase;
    }


    @Override
    public void execute(TelegramUpdateDto payload, ProcessConsumer process) {
        System.out.println("[Core] Processando lógica de negócio da mensagem...");

        if(process.equals(ProcessConsumer.REPORT)) {
            System.out.println("Mensagem é um relatorio, direcionando para o ReportService");
            processReportUseCase.execute(payload);
            return;
        }

        Optional<ParsedExpenseDto> parsed = this.parseMessage(payload.message().text());

        if (parsed.isEmpty()) {
            System.out.println("Mensagem inválida, não foi possível parsear o gasto.");
            return;
        }

        System.out.println("Gasto parseado: " + parsed.get());

        var userCreated = findUserByChatIdUseCase.execute(payload.message().chat().id());

        User user;

        if(userCreated.isPresent()) {
            saveExpenseUseCase.execute(userCreated.get(), payload, parsed.get());
        }else {
            user  = saveUserUseCase.execute(payload);
            saveExpenseUseCase.execute(user, payload, parsed.get());
        }

    }


    private Optional<ParsedExpenseDto> parseMessage(String input) {

        //remove espaços extras no começo/fim da string com o trim()
        String[] tokens = input.trim().split("\\s+"); // quebra a string em um array de palavras, usando um ou mais espaços como separador usando regex e split

        List<String> numericosNoFim = new ArrayList<>();
        int i = tokens.length - 1;

        // anda do fim pro começo enquanto for número, no máximo 2 (valor + parcelas)
        while (i >= 0 && numericosNoFim.size() < 2 && isNumerico(tokens[i])) {
            numericosNoFim.add(0, tokens[i]);
            i--;
        }

        if (numericosNoFim.isEmpty()) {
            return Optional.empty(); // nenhum número encontrado -> mensagem inválida
        }

        String nome = String.join(" ", Arrays.asList(tokens).subList(0, i + 1));
        if (nome.isBlank()) {
            return Optional.empty(); // só tinha número, sem nome
        }

        BigDecimal valor;
        int parcelas = 1;

        if (numericosNoFim.size() == 2) {
            valor = parseValor(numericosNoFim.get(0));
            parcelas = Integer.parseInt(numericosNoFim.get(1));
        } else {
            valor = parseValor(numericosNoFim.getFirst());
        }

        if (valor == null || valor.signum() <= 0) {
            return Optional.empty();
        }
        if (parcelas < 1 || parcelas > 99) {
            return Optional.empty();
        }

        return Optional.of(new ParsedExpenseDto(nome, valor, parcelas));
    }

    private boolean isNumerico(String token) {
        return token.matches("\\d+([.,]\\d{1,2})?");
    }

    private BigDecimal parseValor(String token) {
        try {
            return new BigDecimal(token.replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
