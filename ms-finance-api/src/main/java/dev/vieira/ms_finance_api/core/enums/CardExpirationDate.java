package dev.vieira.ms_finance_api.core.enums;

import dev.vieira.ms_finance_api.core.exception.BusinessException;
import lombok.Getter;

@Getter
public enum CardExpirationDate {
    VENCIMENTO_CINCO(5),
    VENCIMENTO_DEZ(10),
    VENCIMENTO_QUINZE(15),
    VENCIMENTO_VINTE(20),
    VENCIMENTO_VINTE_CINCO(25);

    private final int value;

    CardExpirationDate(int value) {
        this.value = value;
    }

    public static CardExpirationDate fromValue(int value) {
        for (CardExpirationDate day : CardExpirationDate.values()) {
            if (day.getValue() == value) {
                return day;
            }
        }
        throw new BusinessException("Dia de vencimento inválido: " + value);
    }

}
