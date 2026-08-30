package dev.vieira.ms_finance_api.infrastructure.resources.client;

import dev.vieira.ms_finance_api.infrastructure.dto.GeminiDto.GeminiRequestDto;
import dev.vieira.ms_finance_api.infrastructure.dto.GeminiDto.GeminiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "geminiClient", url = "${gemini.api.url}")
public interface GeminiClient {

    @PostMapping("/v1beta/models/gemini-3.1-flash-lite:generateContent")
    GeminiResponseDto generate(
            @RequestParam("key") String apiKey,
            @RequestBody GeminiRequestDto request
    );
}
