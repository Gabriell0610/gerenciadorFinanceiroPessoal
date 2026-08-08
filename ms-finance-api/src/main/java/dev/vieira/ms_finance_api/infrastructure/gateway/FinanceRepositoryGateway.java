package dev.vieira.ms_finance_api.infrastructure.gateway;


import dev.vieira.ms_finance_api.core.dto.Report.NotificationResponseDto;
import dev.vieira.ms_finance_api.core.entities.Category;
import dev.vieira.ms_finance_api.core.entities.Expense;
import dev.vieira.ms_finance_api.core.entities.Report;
import dev.vieira.ms_finance_api.core.entities.User;
import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;
import dev.vieira.ms_finance_api.infrastructure.dto.GeminiDto.GeminiRequestDto;
import dev.vieira.ms_finance_api.infrastructure.dto.GeminiDto.GeminiResponseDto;
import dev.vieira.ms_finance_api.infrastructure.mapper.UserMapper;
import dev.vieira.ms_finance_api.infrastructure.persistence.expense.ExpenseEntity;
import dev.vieira.ms_finance_api.infrastructure.persistence.expense.ExpenseRepository;
import dev.vieira.ms_finance_api.infrastructure.persistence.report.ReportEntity;
import dev.vieira.ms_finance_api.infrastructure.persistence.report.ReportRepository;
import dev.vieira.ms_finance_api.infrastructure.persistence.user.UserEntity;
import dev.vieira.ms_finance_api.infrastructure.persistence.user.UserRepository;
import dev.vieira.ms_finance_api.infrastructure.resources.client.GeminiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static dev.vieira.ms_finance_api.infrastructure.config.RabbitMQConfig.EXCHANGE;
import static dev.vieira.ms_finance_api.infrastructure.config.RabbitMQConfig.ROUTING_KEY_NOTIFICATION_RESPONSE;

@Component
@RequiredArgsConstructor
public class FinanceRepositoryGateway implements FinanceGateway {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ReportRepository reportRepository;
    private final RabbitTemplate rabbitTemplate;
    private final GeminiClient geminiClient;

    @Value("${gemini.token}")
    private String apiKey;

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

        System.out.println("FinanceRepositoryGateway.findUserByChatId: result = " + result);
        return result;
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
    public Report findReportByUserId(UUID userId) {
        return null;
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

    @Override
    public GeminiResponseDto processMessageIA(String message) {

        String prompt = """
        Você é um assistente financeiro. Analise a mensagem e extraia as informações do gasto.
        
        Data atual: %s
        
        Retorne APENAS um JSON válido, sem texto adicional, sem markdown, sem ```json.
        
        Formato:
        {
          "description": "Onde o dinheiro foi gasto/estabelecimento",
          "amount": valor numérico,
          "category": "categoria",
          "installments": número inteiro,
          "paymentDate": "YYYY-MM-DD"
        }
        
        Categorias: Alimentação, Transporte, Saúde, Lazer, Moradia, Vestuario, Outros.
        
        Regras de parcelas:
        - Se o usuário não mencionar quantidade de parcelas, installments = 1.
        - Se mencionar parcelas de qualquer forma ("3 vezes", "três vezes", "parcelei em 3", ou apenas o número após o valor), use esse número.
        - Exemplos: "kart 345 3", "kart 345 parcelei em 3", "kart 345 três vezes" → installments = 3.
        - Exemplos sem parcela: "uber 50", "gastei 50 no uber" → installments = 1.
        
        Regras de data:
        - Se o usuário não mencionar mês, use a data atual como paymentDate.
        - Se o usuário mencionar um mês ("para setembro", "coloque em outubro"), use o dia 10 desse mês no ano atual, pois indica uma compra no crédito com vencimento naquele mês.
        - Exemplos: "uber 50" → paymentDate = data atual. "tênis 71,16 coloque para setembro" → paymentDate = 2026-09-10.
        
        Mensagem: "%s"
        """.formatted(LocalDate.now(), message);

        var parts = new GeminiRequestDto.Parts(prompt);
        var contents = new GeminiRequestDto.Contents(List.of(parts));
        var request = new GeminiRequestDto(List.of(contents));

        try {
            var result = geminiClient.generate(apiKey, request);
            System.out.println("Resultado da api do gmini: " + result);
            return result;
        }catch(Exception e) {
            System.out.println("[GEMINI] Falha ao processar mensagem: " + e.getMessage());
            throw new RuntimeException("Falha ao processar mensagem com Gemini", e);
        }

    }


//    @Override
//    public void sendReportFile(byte[] csv, Long chatId) {
//        notificationClient.sendFile(String.valueOf(chatId), csv);
//    }


}
