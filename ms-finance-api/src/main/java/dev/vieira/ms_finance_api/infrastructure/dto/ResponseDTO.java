package dev.vieira.ms_finance_api.infrastructure.dto;

import dev.vieira.ms_finance_api.core.enums.ResponseType;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseDTO {

    private LocalDateTime date;        // Data e hora da resposta
    private String message;            // Mensagem amigável para o front
    private String detail;             // Detalhes técnicos (opcional)
    private ResponseType responseType; // Tipo da resposta (erro, sucesso, warning)
    private Object data;               // Dados ou detalhes do erro
}
