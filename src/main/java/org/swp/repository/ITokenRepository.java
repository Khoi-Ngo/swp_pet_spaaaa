package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.swp.entity.other.PasswordResetToken;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ITokenRepository extends JpaRepository<PasswordResetToken, Integer> {

    Optional<PasswordResetToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.expiryDateTime <= :now")
    void deleteAllExpiredSince(@Param("now") LocalDateTime now);
}
