package dev.vieira.ms_finance_api.infrastructure.gateway;


import dev.vieira.ms_finance_api.core.entities.Category;
import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.entities.User;
import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;
import dev.vieira.ms_finance_api.infrastructure.persistence.expense.ExpenseEntity;
import dev.vieira.ms_finance_api.infrastructure.persistence.expense.ExpenseRepository;
import dev.vieira.ms_finance_api.infrastructure.persistence.user.UserEntity;
import dev.vieira.ms_finance_api.infrastructure.persistence.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FinanceRepositoryGateway implements FinanceGateway {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        var userEntity = UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .chatId(user.getChatId())
                .linkCode(null)
                .codeExpiresAt(null)
                .created_at(user.getCreated_at())
                .build();

        userRepository.save(userEntity); // Salva o usuário no banco de dados
        return user;
    }

    @Override
    public Optional<User> findUserById(UUID userId) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findUserByTelegramId(String telegramId) {
        return Optional.empty();
    }

    @Override
    public Optional<Category> findCategoryById(UUID categoryId) {
        return Optional.empty();
    }

    @Override
    public Expense saveExpense(Expense expense) {
        //Fazer mapper da etidade CORE para entidade JPA e salvar no banco

        var userRef = UserEntity.builder()
                .id(expense.getUserId())
                .build();
        var expenseEntity = ExpenseEntity.builder()
                .id(expense.getId())
                .dateExpense(expense.getDateExpense())
                .amount(expense.getAmount())
                .description(expense.getDescription())
                .messageUser(expense.getMessageUser())
                .created_at(expense.getCreated_at())
                .user(userRef)
                .build();

        expenseRepository.save(expenseEntity); // Salva a despesa no banco de dados
        return expense;
    }

    @Override
    public Optional<Expense> findExpenseById(UUID expenseId) {
        return Optional.empty();
    }
}
