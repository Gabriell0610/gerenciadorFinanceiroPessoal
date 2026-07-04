package dev.vieira.ms_finance_api.infrastructure.exception;


import dev.vieira.ms_finance_api.infrastructure.dto.ResponseDTO;
import dev.vieira.ms_finance_api.infrastructure.enums.ResponseType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseDTO> handlerBusinessException(BusinessException ex) {
        var response = ResponseDTO.builder()
                .message(ex.getMessage())
                .codeStatus(ex.getStatus().value())
                .date(LocalDateTime.now())
                .responseType(ResponseType.ERROR)
                .build();

        return ResponseEntity.status(ex.getStatus()).body(response);
    }

}
