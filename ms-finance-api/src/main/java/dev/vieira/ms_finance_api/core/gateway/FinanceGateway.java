package dev.vieira.ms_finance_api.core.gateway;

import dev.vieira.ms_finance_api.core.entities.Category;
import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.entities.User;

import java.util.Optional;
import java.util.UUID;

public interface FinanceGateway {

    User saveUser(User user);
    Optional<User> findUserById(UUID userId);
    Optional<User> findUserByTelegramId(String telegramId); // bot vai precisar desse método

    Optional<Category> findCategoryById(UUID categoryId);

    Expense saveExpense(Expense expense);
    Optional<Expense> findExpenseById(UUID expenseId);
}
