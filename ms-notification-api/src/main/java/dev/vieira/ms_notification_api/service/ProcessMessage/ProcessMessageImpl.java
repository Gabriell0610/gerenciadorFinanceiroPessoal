package dev.vieira.ms_notification_api.service.ProcessMessage;

import dev.vieira.ms_notification_api.dto.TelegramUpdateDto;
import dev.vieira.ms_notification_api.service.ExpenseService.ExpenseServiceImpl;
import dev.vieira.ms_notification_api.service.ReportService.ReportServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProcessMessageImpl implements ProcessTelegramMessageService {

    private final Validator validator;
    private final ReportServiceImpl reportService;
    private final ExpenseServiceImpl expenseService;

    @Override
    public void process(TelegramUpdateDto message) {
        System.out.println("Inicando processo: " + message);

        Set<ConstraintViolation<TelegramUpdateDto>> violations = validator.validate(message);

        if (!violations.isEmpty()) {

            for (ConstraintViolation<TelegramUpdateDto> erro : violations) {
                System.out.println("Erro encontrado: " + erro.getMessage());
            }

            return;
        }

        if(message.message().text().startsWith("/relatorio")) {
            System.out.println("Mensagem é um relatorio, direcionando para o ReportService");
            reportService.execute(message);
        }else {
            System.out.println("Mensagem é um expense, direcionando para o ExpenseService: " + message);
            expenseService.execute(message);
        }

    }
}
