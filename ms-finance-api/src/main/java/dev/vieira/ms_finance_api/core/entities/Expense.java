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
    private LocalDateTime created_at;

    public Expense(UUID userId, BigDecimal amount, String messageUser, String description) {
        this.id = UUID.randomUUID(); // Sistema gera o ID único agora
        this.userId = userId;
        this.amount = amount;
        this.messageUser = messageUser;
        this.description = description;
        this.created_at = LocalDateTime.now();
        this.dateExpense = LocalDateTime.now();
    }

    public Expense(UUID id, UUID userId, BigDecimal amount, String messageUser, String description) {
        this.id = id; // Mantém o ID que veio do banco
        this.userId = userId;
        this.amount = amount;
        this.messageUser = messageUser;
        this.description = description;
    }

    public UUID getId() {
        return id;
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

    public LocalDateTime getCreated_at() {
        return created_at;
    }
}
