package com.rudykart.limbah.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rudykart.limbah.entities.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
