package dev.vieira.ms_finance_api.infrastructure.persistence.Card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, UUID> {

    Optional<CardEntity> findByUser_IdAndName(UUID userId, String name);

}
