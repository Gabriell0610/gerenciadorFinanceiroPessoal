package dev.vieira.ms_finance_api.infrastructure.persistence.expense;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, UUID> {

    List<ExpenseEntity> findAllByUserId(UUID userId);
}
