package dev.vieira.ms_finance_api.infrastructure.dto.GeminiDto;

import java.util.List;

public record GeminiResponseDto(
        List<Candidates> candidates
) {
    public record Candidates(Content content) {}
    public record Content(List<Parts> parts){}
    public record Parts(String text){}

}
