package dev.vieira.ms_finance_api.infrastructure.persistence.user;

import dev.vieira.ms_finance_api.infrastructure.persistence.expense.ExpenseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserEntity  implements Persistable<UUID> {

    @Id
    private UUID id;

    private String name;

    private String email;

    @Column(nullable = false)
    private Long chatId;

    private String linkCode;

    private LocalDateTime codeExpiresAt;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
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
