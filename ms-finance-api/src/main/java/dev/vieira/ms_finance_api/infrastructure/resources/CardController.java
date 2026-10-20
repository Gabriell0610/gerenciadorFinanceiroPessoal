package dev.vieira.ms_finance_api.infrastructure.resources;

import dev.vieira.ms_finance_api.core.entities.Card;
import dev.vieira.ms_finance_api.core.useCase.Card.CardUseCase;
import dev.vieira.ms_finance_api.infrastructure.dto.Card.CardDtoResponse;
import dev.vieira.ms_finance_api.infrastructure.dto.Card.CreateCardDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/card/v1")
@RequiredArgsConstructor
public class CardController {

    private final CardUseCase cardUseCase;

    @PostMapping()
    public ResponseEntity<Card> createCard(@Valid @RequestBody CreateCardDto dto) {

        var cardCreated = cardUseCase.createCard(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(cardCreated);

    }

}
