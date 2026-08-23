package dev.vieira.ms_finance_api.core.gateway;

import dev.vieira.ms_finance_api.core.dto.Report.NotificationResponseDto;
import dev.vieira.ms_finance_api.core.entities.Category;
import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.entities.Report;
import dev.vieira.ms_finance_api.core.entities.User;
import dev.vieira.ms_finance_api.infrastructure.dto.GeminiDto.GeminiResponseDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FinanceGateway {

    User saveUser(User user);
    Optional<User> findUserById(UUID userId);
    Optional<User> findUserByChatId(Long chatId);

    Expense saveExpense(Expense expense);
    Optional<Expense> findExpenseById(UUID expenseId);
    List<Expense> findAllExpenseByUserId(UUID userId);

    Report saveReport(Report report);
    Optional<Report> findReportByUserAndCompetency(UUID userId, java.time.LocalDate competency);

    void sendMessage(NotificationResponseDto message);
}
