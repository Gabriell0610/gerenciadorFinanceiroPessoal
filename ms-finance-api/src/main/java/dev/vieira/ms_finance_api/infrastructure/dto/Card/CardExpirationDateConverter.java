package dev.vieira.ms_finance_api.infrastructure.dto.Card;

import dev.vieira.ms_finance_api.core.enums.CardExpirationDate;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CardExpirationDateConverter implements AttributeConverter<CardExpirationDate, Integer> {

    @Override
    public Integer convertToDatabaseColumn(CardExpirationDate attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public CardExpirationDate convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : CardExpirationDate.fromValue(dbData);
    }
}