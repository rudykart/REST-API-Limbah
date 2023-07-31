package com.rudykart.limbah.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rudykart.limbah.entities.Cash;

public interface CashRepository extends JpaRepository<Cash, Long> {
    
    Page<Cash> findByDescriptionContains(String description, Pageable pageable);

    Optional<Cash> findFirstByOrderByCreatedAtDesc();
}
