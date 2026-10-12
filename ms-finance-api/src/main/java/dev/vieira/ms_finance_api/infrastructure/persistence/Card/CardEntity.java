package dev.vieira.ms_finance_api.infrastructure.persistence.Card;

import dev.vieira.ms_finance_api.infrastructure.persistence.expense.ExpenseEntity;
import dev.vieira.ms_finance_api.infrastructure.persistence.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

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
public class CardEntity {

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

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpenseEntity> expenses;
}
