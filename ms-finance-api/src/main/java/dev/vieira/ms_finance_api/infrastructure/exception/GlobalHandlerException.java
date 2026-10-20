package dev.vieira.ms_finance_api.infrastructure.exception;


import dev.vieira.ms_finance_api.core.exception.BusinessException;
import dev.vieira.ms_finance_api.core.exception.ExternalServiceException;
import dev.vieira.ms_finance_api.infrastructure.dto.ResponseDTO;
import dev.vieira.ms_finance_api.core.enums.ResponseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseDTO> handlerBusinessException(BusinessException ex) {
        ResponseDTO response = ResponseDTO.builder()
                .message(ex.getMessage())
                .date(LocalDateTime.now())
                .responseType(ResponseType.ERROR)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ResponseDTO> handleExternalServiceException(ExternalServiceException  ex) {

        log.error("Erro ao comunicar com serviço externo", ex);

        ResponseDTO response = ResponseDTO.builder()
                .message("O serviço externo está temporariamente indisponível.")
                .date(LocalDateTime.now())
                .responseType(ResponseType.ERROR)
                .build();

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleUnexpectedException(Exception ex) {

        ResponseDTO response = ResponseDTO.builder()
                .message("Ocorreu um erro interno.")
                .date(LocalDateTime.now())
                .responseType(ResponseType.ERROR)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

