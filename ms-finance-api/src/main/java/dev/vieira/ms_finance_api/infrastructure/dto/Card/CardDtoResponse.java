package dev.vieira.ms_finance_api.infrastructure.dto.Card;

import dev.vieira.ms_finance_api.core.enums.CardExpirationDate;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record CardDtoResponse(
        String name,
        UUID userId,
        CardExpirationDate expirationDate,
        LocalDateTime created_at,
        LocalDateTime updated_at,
        BigDecimal invoice) {
}
