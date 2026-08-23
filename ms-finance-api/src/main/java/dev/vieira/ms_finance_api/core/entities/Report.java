package dev.vieira.ms_finance_api.core.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Report {

    private final UUID id;
    private final UUID userId;
    private LocalDate competency;
    private BigDecimal totalExpenses;
    private BigDecimal totalInstallments;

    public boolean isNew() {
        return isNew;
    }

    private BigDecimal grandTotal;
    private int itemCount;
    private LocalDateTime generatedAt;
    private LocalDateTime updatedAt;
    private boolean isNew;

    public Report(UUID id, UUID userId, LocalDate competency, BigDecimal totalExpenses, BigDecimal totalInstallments, BigDecimal grandTotal, int itemCount) {
        this.id = id != null ? id : UUID.randomUUID();
        this.isNew = id == null;
        this.userId = userId;
        this.competency = competency;
        this.totalExpenses = totalExpenses;
        this.totalInstallments = totalInstallments;
        this.grandTotal = grandTotal;
        this.itemCount = itemCount;
        this.generatedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

    }

    public Report(UUID id, UUID userId, LocalDate competency, BigDecimal totalExpenses, BigDecimal totalInstallments, BigDecimal grandTotal, int itemCount, LocalDateTime updatedAt, LocalDateTime generatedAt) {
        this.id = id;
        this.isNew = false;
        this.userId = userId;
        this.competency = competency;
        this.totalExpenses = totalExpenses;
        this.totalInstallments = totalInstallments;
        this.grandTotal = grandTotal;
        this.itemCount = itemCount;
        this.updatedAt = updatedAt;
        this.generatedAt = generatedAt;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public LocalDate getCompetency() {
        return competency;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public int getItemCount() {
        return itemCount;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public BigDecimal getTotalInstallments() {
        return totalInstallments;
    }

}
