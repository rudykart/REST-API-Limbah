package com.rudykart.limbah.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rudykart.limbah.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findByNameContains(String Name, Pageable pageable);
}
