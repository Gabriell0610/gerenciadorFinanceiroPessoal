package dev.vieira.ms_finance_api.core.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Expense {

    private final UUID id;
    private UUID categoryId;
    private UUID userId;
    private BigDecimal amount;
    private LocalDateTime dateExpense;
    private String messageUser;
    private String description;

    public Expense(UUID userId, UUID categoryId, BigDecimal amount, String messageUser, String description) {
        this.id = UUID.randomUUID(); // Sistema gera o ID único agora
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.dateExpense = LocalDateTime.now(); // Sistema captura o momento exato do gasto automaticamente
        this.messageUser = messageUser;
        this.description = description;
    }

    public Expense(UUID id, UUID userId, UUID categoryId, BigDecimal amount, LocalDateTime dateExpense, String messageUser, String description) {
        this.id = id; // Mantém o ID que veio do banco
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.dateExpense = dateExpense; // Mantém a data original do banco
        this.messageUser = messageUser;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public UUID getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getDateExpense() {
        return dateExpense;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public String getDescription() {
        return description;
    }
}
