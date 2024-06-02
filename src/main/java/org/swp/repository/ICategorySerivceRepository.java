package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.swp.entity.ServiceCategory;
@Repository
public interface ICategorySerivceRepository extends JpaRepository<ServiceCategory, Integer> {
}
