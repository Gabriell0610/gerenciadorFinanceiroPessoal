package dev.vieira.ms_finance_api.infrastructure.persistence.report;

import dev.vieira.ms_finance_api.core.enums.ReportStatus;
import dev.vieira.ms_finance_api.infrastructure.persistence.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "report")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReportEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "competency", nullable = false)
    private LocalDate competency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;

    @Column(name = "total_expenses", nullable = false)
    private BigDecimal totalExpenses;

    @Column(name = "total_installments", nullable = false)
    private BigDecimal totalInstallments;

    @Column(name = "grand_total", nullable = false)
    private BigDecimal grandTotal;

    @Column(name = "item_count", nullable = false)
    private Integer itemCount;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Transient
    @Builder.Default
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PostPersist
    void markNotNew() {
        this.isNew = false;
    }
}
