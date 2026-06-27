package dev.vieira.ms_finance_api.core.usecases;

import dev.vieira.ms_finance_api.core.dto.Expense.ParsedExpenseDto;
import dev.vieira.ms_finance_api.core.dto.TelegramDto.TelegramUpdateDto;
import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.entities.User;
import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProcessMessageImpl implements ProcessMessageUseCase {

    private final FinanceGateway financeGateway;

    public ProcessMessageImpl(FinanceGateway financeGateway) {
        this.financeGateway = financeGateway;
    }


    @Override
    public void execute(TelegramUpdateDto message) {
        System.out.println("[Core] Processando lógica de negócio da mensagem...");
        Optional<ParsedExpenseDto> parsed = this.parseMessage(message.message().text());

        if(parsed.isPresent()) {
            System.out.println("Gasto parseado: " + parsed.get());

            var userEntity = new User(
                message.message().chat().first_name(), null, message.message().chat().id());

            var expenseEntity = new Expense(userEntity.getId(), parsed.get().valor(), message.message().text(), parsed.get().nome());

            financeGateway.saveUser(userEntity);
            financeGateway.saveExpense(expenseEntity);

        } else {
            System.out.println("Mensagem inválida, não foi possível parsear o gasto.");
        }

        // Exemplo da sua regra de negócio:
        // 1. Extrair os dados do payload (texto, valor, chatId)
        // 2. Buscar o usuário usando: financeGateway.findUserByTelegramId(...)
        // 3. Criar a entidade de domínio Expense e User
        // 4. Salvar usando: financeGateway.saveExpense(novaExpense)
    }


    private Optional<ParsedExpenseDto> parseMessage(String input) {

        //remove espaços extras no começo/fim da string com o trim()
        String[] tokens = input.trim().split("\\s+"); // quebra a string em um array de palavras, usando um ou mais espaços como separador

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
        Integer parcelas = null;

        if (numericosNoFim.size() == 2) {
            valor = parseValor(numericosNoFim.get(0));
            parcelas = Integer.parseInt(numericosNoFim.get(1));
        } else {
            valor = parseValor(numericosNoFim.get(0));
        }

        if (valor == null || valor.signum() <= 0) {
            return Optional.empty();
        }
        if (parcelas != null && (parcelas < 1 || parcelas > 99)) {
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
