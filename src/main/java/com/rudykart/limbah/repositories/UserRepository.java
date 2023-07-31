package com.rudykart.limbah.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;

import com.rudykart.limbah.entities.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // Metode untuk mengambil token terakhir untuk seorang pengguna
    // @Query(
    // "SELECT t FROM Token t JOIN t.user u WHERE u.id = :userId
    // ORDER BY t.expired DESC"
    // )
    // Token findLastTokenByUserId(@Param("userId") Long userId);

    Optional<User> findByEmail(String Email);
}
