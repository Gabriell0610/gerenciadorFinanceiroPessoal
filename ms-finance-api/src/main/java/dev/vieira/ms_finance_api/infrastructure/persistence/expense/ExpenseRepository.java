package dev.vieira.ms_finance_api.infrastructure.persistence.expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, UUID> {


    @Query(value = """
    SELECT * FROM expenses e
    WHERE e.user_id = :userId
    AND DATE_TRUNC('month', e.date_expense) <= DATE_TRUNC('month', CAST(:competency AS date))
    AND (
        EXTRACT(YEAR FROM AGE(
            DATE_TRUNC('month', CAST(:competency AS date)),
            DATE_TRUNC('month', e.date_expense)
        )) * 12 +
        EXTRACT(MONTH FROM AGE(
            DATE_TRUNC('month', CAST(:competency AS date)),
            DATE_TRUNC('month', e.date_expense)
        ))
    ) < e.installment
    """, nativeQuery = true)
    List<ExpenseEntity> findAllUserExpenseWithCompetency(
            @Param("userId") UUID userId,
            @Param("competency") LocalDate competency
    );
}
