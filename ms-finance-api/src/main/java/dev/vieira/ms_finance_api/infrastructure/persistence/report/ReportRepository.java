package dev.vieira.ms_finance_api.infrastructure.persistence.report;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<ReportEntity, UUID> {

    Optional<ReportEntity> findReportByUserIdAndCompetency(UUID userId, LocalDate competency);
}
