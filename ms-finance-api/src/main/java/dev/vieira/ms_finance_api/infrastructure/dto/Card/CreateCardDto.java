package dev.vieira.ms_finance_api.infrastructure.dto.Card;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record CreateCardDto(
        @NotBlank
        String name,

        BigDecimal invoice,

        @NotNull
        UUID userId,

        int expirationDate) {

}
