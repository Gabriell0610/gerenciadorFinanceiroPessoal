package dev.vieira.ms_finance_api.infrastructure.enums;

public enum ResponseType {
    SUCCESS("Sucesso"),
    WARNING("Advertência"),
    ERROR("Erro");

    private final String text;

    ResponseType(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}