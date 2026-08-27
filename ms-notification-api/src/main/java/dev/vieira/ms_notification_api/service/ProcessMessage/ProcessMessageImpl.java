package dev.vieira.ms_notification_api.service.ProcessMessage;

import dev.vieira.ms_notification_api.dto.TelegramUpdateDto;
import dev.vieira.ms_notification_api.service.ExpenseService.ExpenseServiceImpl;
import dev.vieira.ms_notification_api.service.ReportService.ReportServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessMessageImpl implements ProcessTelegramMessageService {

    private final Validator validator;
    private final ReportServiceImpl reportService;
    private final ExpenseServiceImpl expenseService;

    @Override
    public void process(TelegramUpdateDto message) {
        log.info("Inicando processo: {}", message);

        Set<ConstraintViolation<TelegramUpdateDto>> violations = validator.validate(message);

        if (!violations.isEmpty()) {

            for (ConstraintViolation<TelegramUpdateDto> erro : violations) {
                log.error("Erro encontrado: {}", erro.getMessage());
            }

            return;
        }

        if(message.message().text().startsWith("/relatorio")) {
            log.info("Mensagem é um relatorio, direcionando para o ReportService");
            reportService.execute(message);
        }else {
            log.info("Mensagem é um expense, direcionando para o ExpenseService: {}", message);
            expenseService.execute(message);
        }

    }
}
