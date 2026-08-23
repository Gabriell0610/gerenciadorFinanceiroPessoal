package dev.vieira.ms_finance_api.infrastructure.gateway;


import dev.vieira.ms_finance_api.core.dto.Report.NotificationResponseDto;
import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.entities.Report;
import dev.vieira.ms_finance_api.core.entities.User;
import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;
import dev.vieira.ms_finance_api.infrastructure.mapper.UserMapper;
import dev.vieira.ms_finance_api.infrastructure.persistence.expense.ExpenseEntity;
import dev.vieira.ms_finance_api.infrastructure.persistence.expense.ExpenseRepository;
import dev.vieira.ms_finance_api.infrastructure.persistence.report.ReportEntity;
import dev.vieira.ms_finance_api.infrastructure.persistence.report.ReportRepository;
import dev.vieira.ms_finance_api.infrastructure.persistence.user.UserEntity;
import dev.vieira.ms_finance_api.infrastructure.persistence.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static dev.vieira.ms_finance_api.infrastructure.config.RabbitMQConfig.EXCHANGE;
import static dev.vieira.ms_finance_api.infrastructure.config.RabbitMQConfig.ROUTING_KEY_NOTIFICATION_RESPONSE;

@Slf4j
@Component
@RequiredArgsConstructor
public class FinanceRepositoryGateway implements FinanceGateway {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ReportRepository reportRepository;
    private final RabbitTemplate rabbitTemplate;

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
    public Optional<User> findUserByChatId(Long chatId) {
        var result =  userRepository.findUserByChatId(chatId)
                .map(userMapper::toDomain);

        FinanceRepositoryGateway.log.info("FinanceRepositoryGateway.findUserByChatId: result = " + result);
        return result;
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
                .installment(expense.getInstallment())
                .category(expense.getCategory())
                .user(userRef)
                .build();

        expenseRepository.save(expenseEntity); // Salva a despesa no banco de dados
        return expense;
    }

    @Override
    public Optional<Expense> findExpenseById(UUID expenseId) {
        return Optional.empty();
    }

    @Override
    public List<Expense> findAllExpenseByUserId(UUID userId) {
        return expenseRepository.findAllByUserId(userId)
                .stream()
                .map(expenseEntity -> new Expense(
                        expenseEntity.getId(),
                        expenseEntity.getUser().getId(),
                        expenseEntity.getAmount(),
                        expenseEntity.getMessageUser(),
                        expenseEntity.getDescription(),
                        expenseEntity.getInstallment(),
                        expenseEntity.getDateExpense(),
                        expenseEntity.getCreated_at(),
                        expenseEntity.getCategory()
                ))
                .toList();
    }

    @Override
    public Report saveReport(Report report) {
        var userRef = UserEntity.builder()
                .id(report.getUserId())
                .build();

        var reportEntity = ReportEntity.builder()
                .id(report.getId())
                .user(userRef)
                .competency(report.getCompetency())
                .totalExpenses(report.getTotalExpenses())
                .totalInstallments(report.getTotalInstallments())
                .grandTotal(report.getGrandTotal())
                .itemCount(report.getItemCount())
                .generatedAt(report.getGeneratedAt())
                .updatedAt(report.getUpdatedAt())
                .isNew(report.isNew())
                .build();

        reportRepository.save(reportEntity);

        return report;
    }

    @Override
    public Optional<Report> findReportByUserAndCompetency(UUID userId, LocalDate competency) {
        return reportRepository.findReportByUserIdAndCompetency(userId, competency)
                .map(entity -> new Report(
                        entity.getId(),
                        entity.getUser().getId(),
                        entity.getCompetency(),
                        entity.getTotalExpenses(),
                        entity.getTotalInstallments(),
                        entity.getGrandTotal(),
                        entity.getItemCount(),
                        entity.getGeneratedAt(),
                        entity.getUpdatedAt()
                ));
    }

    @Override
    public void sendMessage(NotificationResponseDto message) {
        rabbitTemplate.convertAndSend(
                EXCHANGE,
                ROUTING_KEY_NOTIFICATION_RESPONSE,
                message
        );
    }


}
