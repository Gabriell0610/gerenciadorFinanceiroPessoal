package dev.vieira.ms_finance_api.infrastructure.persistence.expense;

import dev.vieira.ms_finance_api.infrastructure.persistence.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ExpenseEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column(name = "message_user", nullable = false)
    private String messageUser;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "date_expense", nullable = false)
    private LocalDateTime dateExpense;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

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
