package dev.vieira.ms_finance_api.core.useCase.Card;

import dev.vieira.ms_finance_api.core.entities.Card;
import dev.vieira.ms_finance_api.infrastructure.dto.Card.CreateCardDto;

import java.util.Optional;
import java.util.UUID;

public interface CardUseCase {

    Card createCard(CreateCardDto data);
    Optional<Card> findCardByUserIdAndNameCard(String cardName, UUID userId);
}
