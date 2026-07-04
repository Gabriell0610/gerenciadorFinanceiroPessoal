package dev.vieira.ms_finance_api.infrastructure.mapper;

import dev.vieira.ms_finance_api.core.entities.User;
import dev.vieira.ms_finance_api.infrastructure.persistence.user.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getChatId(),
                entity.getName(),
                entity.getEmail(),
                entity.getLinkCode(),
                entity.getCodeExpiresAt(),
                entity.getCreated_at()
        );
    }
}
