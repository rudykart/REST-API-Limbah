package com.rudykart.limbah.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rudykart.limbah.entities.MaterialPurchase;

public interface MaterialPurchaseRepository extends JpaRepository<MaterialPurchase, Long> {
    @Query("SELECT mp FROM MaterialPurchase mp WHERE mp.purchase.id = :purchaseId")
    List<MaterialPurchase> findByPurchaseId(Long purchaseId);
}
