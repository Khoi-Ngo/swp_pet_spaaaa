package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swp.entity.other.PasswordResetToken;

public interface ITokenRepository extends JpaRepository<PasswordResetToken, Integer> {

    PasswordResetToken findByToken(String token);

}
