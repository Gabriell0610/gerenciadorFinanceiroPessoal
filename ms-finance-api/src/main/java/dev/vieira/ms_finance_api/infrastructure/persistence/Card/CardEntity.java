package dev.vieira.ms_finance_api.infrastructure.persistence.Card;

import dev.vieira.ms_finance_api.core.enums.CardExpirationDate;
import dev.vieira.ms_finance_api.infrastructure.dto.Card.CardExpirationDateConverter;
import dev.vieira.ms_finance_api.infrastructure.persistence.expense.ExpenseEntity;
import dev.vieira.ms_finance_api.infrastructure.persistence.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "card")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class CardEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @Column(name = "invoice", nullable = false)
    private BigDecimal invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Convert(converter = CardExpirationDateConverter.class)
    @Column(name = "due_date")
    private CardExpirationDate expirationDate;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpenseEntity> expenses;

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
