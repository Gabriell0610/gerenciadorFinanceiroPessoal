package dev.vieira.ms_finance_api.core.gateway;

import dev.vieira.ms_finance_api.core.dto.Report.NotificationResponseDto;
import dev.vieira.ms_finance_api.core.entities.*;
import dev.vieira.ms_finance_api.infrastructure.dto.Card.CardDtoResponse;
import dev.vieira.ms_finance_api.infrastructure.dto.Card.CreateCardDto;
import dev.vieira.ms_finance_api.infrastructure.dto.GeminiDto.GeminiResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FinanceGateway {

    User saveUser(User user);
    Optional<User> findUserById(UUID userId);
    Optional<User> findUserByChatId(Long chatId);

    Expense saveExpense(Expense expense);
    Optional<Expense> findExpenseById(UUID expenseId);
    List<Expense> findAllUserExpenseWithCompetency(UUID userId, LocalDate competency);

    Report saveReport(Report report);
    Optional<Report> findReportByUserAndCompetency(UUID userId, java.time.LocalDate competency);

    void sendMessage(NotificationResponseDto message);

    Optional<Card> findCardByUserIdAndNameCard(String cardName, UUID userId);
    Card createCard(Card card);
}
