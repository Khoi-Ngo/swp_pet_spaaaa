package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.swp.entity.User;

import java.util.Collection;

public interface IAdminRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.role = 1")
    Collection<Object> findAllCustomerACC();

}
