package dev.vieira.ms_finance_api.core.entities;

import dev.vieira.ms_finance_api.core.enums.CardExpirationDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Card {

    private final UUID id;
    private String name;
    private UUID userId;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private CardExpirationDate dueDate;
    private BigDecimal invoice;

    public Card(UUID userId, String name, CardExpirationDate dueDate, BigDecimal invoice) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.name = name;
        this.dueDate = dueDate;
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
        this.invoice = invoice;
    }

    public Card(UUID id, BigDecimal invoice, LocalDateTime updated_at, LocalDateTime created_at, UUID userId,
                String name, CardExpirationDate dueDate) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.dueDate = dueDate;
        this.invoice = invoice;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UUID getUserId() {
        return userId;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public BigDecimal getInvoice() {
        return invoice;
    }

    public CardExpirationDate getDueDate() {
        return dueDate;
    }
}
