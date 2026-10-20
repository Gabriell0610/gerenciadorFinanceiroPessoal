package dev.vieira.ms_finance_api.core.useCase.Card;

import dev.vieira.ms_finance_api.core.entities.Card;
import dev.vieira.ms_finance_api.core.enums.CardExpirationDate;
import dev.vieira.ms_finance_api.core.gateway.FinanceGateway;
import dev.vieira.ms_finance_api.infrastructure.dto.Card.CreateCardDto;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class CardImpl implements CardUseCase{

    private FinanceGateway financeGateway;

    public CardImpl (FinanceGateway financeGateway) {
        this.financeGateway = financeGateway;
    }

    @Override
    public Card createCard(CreateCardDto dto) {
        var invoice = dto.invoice();
        var expirationDate = dto.expirationDate();
        if(expirationDate == 0) {
            expirationDate = CardExpirationDate.VENCIMENTO_CINCO.getValue();
        }

        var dueDate = CardExpirationDate.fromValue(expirationDate);
        var nameParsed = dto.name().toLowerCase();

        if(invoice == null) {
            invoice = BigDecimal.ZERO;
        }


        var cardEntity = new Card(dto.userId() , nameParsed, dueDate, invoice);

        return financeGateway.createCard(cardEntity);
    }

    @Override
    public Optional<Card> findCardByUserIdAndNameCard(String cardName, UUID userId) {
        System.out.println("caiu aqui no findCardByUserIdAndNameCard");
        return financeGateway.findCardByUserIdAndNameCard(cardName, userId);
    }


}
