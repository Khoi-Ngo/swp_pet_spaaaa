package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swp.entity.User;

public interface IAdminRepository extends JpaRepository<User, Integer> {
}
