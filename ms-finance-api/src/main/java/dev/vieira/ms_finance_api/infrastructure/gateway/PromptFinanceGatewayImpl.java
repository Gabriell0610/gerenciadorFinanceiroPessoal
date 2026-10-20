package dev.vieira.ms_finance_api.infrastructure.gateway;

import dev.vieira.ms_finance_api.core.exception.ExternalServiceException;
import dev.vieira.ms_finance_api.core.gateway.PromptFinanceGateway;
import dev.vieira.ms_finance_api.infrastructure.dto.GeminiDto.GeminiRequestDto;
import dev.vieira.ms_finance_api.infrastructure.dto.GeminiDto.GeminiResponseDto;
import dev.vieira.ms_finance_api.infrastructure.resources.client.GeminiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PromptFinanceGatewayImpl implements PromptFinanceGateway {

    private final GeminiClient geminiClient;

    @Value("${gemini.token}")
    private String apiKey;


    @Override
    public GeminiResponseDto processMessage(String message) {
        String prompt = """
        Você é um assistente financeiro. Analise a mensagem e extraia as informações do gasto.
        
        Data atual: %s
        
        Retorne APENAS um JSON válido, sem texto adicional, sem markdown, sem ```json.
        
        Formato:
        {
          "description": "Onde o dinheiro foi gasto/estabelecimento",
          "amount": valor numérico,
          "category": "categoria",
          "installments": número inteiro,
          "cardName": nome do cartão que foi efetuado o pagamento,
          "paymentDate": "YYYY-MM-DD"
        }
        
        Categorias: Alimentação, Transporte, Saúde, Lazer, Moradia, Vestuario, Outros.
        
        Regras de parcelas:
        - Se o usuário não mencionar quantidade de parcelas, installments = 1.
        - Se mencionar parcelas de qualquer forma ("3 vezes", "três vezes", "parcelei em 3", ou apenas o número após o valor), use esse número.
        - Exemplos: "kart 345 3", "kart 345 parcelei em 3", "kart 345 três vezes" → installments = 3.
        - Exemplos sem parcela: "uber 50", "gastei 50 no uber" → installments = 1.
        
        Regras de data:
        - Se o usuário não mencionar mês, use a data atual como paymentDate.
        - Se o usuário mencionar um mês ("para setembro", "coloque em outubro"), use o dia 10 desse mês no ano atual, pois indica uma compra no crédito com vencimento naquele mês.
        - Exemplos: "uber 50" → paymentDate = data atual. "tênis 71,16 coloque para setembro" → paymentDate = 2026-09-10.
        
        Mensagem: "%s"
        """.formatted(LocalDate.now(), message);

        var parts = new GeminiRequestDto.Parts(prompt);
        var contents = new GeminiRequestDto.Contents(List.of(parts));
        var request = new GeminiRequestDto(List.of(contents));

        try {
            var result = geminiClient.generate(apiKey, request);
            return result;
        }catch(Exception e) {
            log.error("[GEMINI] Erro ao comunicar com Gemini", e);

            throw new ExternalServiceException(
                    "Não foi possível processar a mensagem."
            );
        }
    }
}
