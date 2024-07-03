package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.swp.entity.User;
import org.swp.enums.UserRole;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT role FROM tbl_user where username = :userName", nativeQuery = true)
    Integer findRoleByUserName(@Param("userName") String userName);
}
