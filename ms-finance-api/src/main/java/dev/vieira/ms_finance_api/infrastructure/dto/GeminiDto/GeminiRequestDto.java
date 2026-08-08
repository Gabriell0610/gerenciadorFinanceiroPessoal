package dev.vieira.ms_finance_api.infrastructure.dto.GeminiDto;

import java.util.List;

public record GeminiRequestDto(
        List<Contents> contents
) {

    public record Contents(List<Parts> parts){}
    public record Parts(String text){}}
