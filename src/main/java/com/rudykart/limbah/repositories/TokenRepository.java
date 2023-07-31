package com.rudykart.limbah.repositories;

// import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;

import com.rudykart.limbah.entities.user.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {

    // @Query("""
    // select t from Token inner join User u on t.user.id u.id
    // where u.id = :userId and (t.expired = false or t.revoked = false)
    // """)
    // @Query("select t from Token t inner join t.user u where u.id = :userId and
    // (t.expired = false or t.revoked = false)")
    // List<Token> findAllValidTokensByUser(Long userId);

    Optional<Token> findByToken(String token);
}
