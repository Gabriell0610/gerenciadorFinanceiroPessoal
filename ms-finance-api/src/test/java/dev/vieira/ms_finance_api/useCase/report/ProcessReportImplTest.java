package dev.vieira.ms_finance_api.useCase.report;

import dev.vieira.ms_finance_api.core.useCase.expense.FindAllExpenseByUserIdUseCase;
import dev.vieira.ms_finance_api.core.useCase.report.contract.FindReportByUserAndCompetencyUseCase;
import dev.vieira.ms_finance_api.core.useCase.report.contract.SaveReportUseCase;
import dev.vieira.ms_finance_api.core.useCase.report.contract.SendNotificationUseCase;
import dev.vieira.ms_finance_api.core.useCase.report.impl.ProcessReportImpl;
import dev.vieira.ms_finance_api.core.useCase.user.FindUserByChatIdUseCase;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProcessReportImplTest {

    @Mock
    private  FindUserByChatIdUseCase findUserByChatIdUseCase;
    @Mock
    private  FindAllExpenseByUserIdUseCase findAllExpenseByUserIdUseCase;
    @Mock
    private  FindReportByUserAndCompetencyUseCase findReportByUserAndCompetencyUseCase;
    @Mock
    private  SaveReportUseCase saveReportUseCase;
    @Mock
    private  SendNotificationUseCase sendNotificationUseCase;

    @InjectMocks
    private ProcessReportImpl processReportImpl;


}
