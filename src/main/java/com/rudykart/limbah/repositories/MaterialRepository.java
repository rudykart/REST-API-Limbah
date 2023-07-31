package com.rudykart.limbah.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rudykart.limbah.entities.Material;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    Page<Material> findByNameContains(String Name, Pageable pageable);

}
