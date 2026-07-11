package dev.vieira.ms_finance_api.core.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Expense {

    private final UUID id;
    private UUID userId;
    private BigDecimal amount;
    private LocalDateTime dateExpense;
    private String messageUser;
    private String description;
    private LocalDateTime created_at;
    private Integer installment;
    private String category;

    public Expense(UUID userId, BigDecimal amount, String messageUser,
                   String description, Integer installment, String category) {
        this.id = UUID.randomUUID(); // Sistema gera o ID único agora
        this.userId = userId;
        this.amount = amount;
        this.messageUser = messageUser;
        this.description = description;
        this.created_at = LocalDateTime.now();
        this.dateExpense = LocalDateTime.now();
        this.installment = installment;
        this.category = category;
    }

    public Expense(UUID id, UUID userId, BigDecimal amount, String messageUser, String description,
                   Integer installment,LocalDateTime dateExpense, LocalDateTime created_at, String category ) {
        this.id = id; // Mantém o ID que veio do banco
        this.userId = userId;
        this.amount = amount;
        this.messageUser = messageUser;
        this.description = description;
        this.installment = installment;
        this.dateExpense = dateExpense;
        this.created_at  = created_at;
        this.category = category;
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

    public Integer getInstallment() {
        return installment;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", userId=" + userId +
                ", amount=" + amount +
                ", dateExpense=" + dateExpense +
                ", messageUser='" + messageUser + '\'' +
                ", description='" + description + '\'' +
                ", created_at=" + created_at +
                ", installment=" + installment +
                '}';
    }
}
