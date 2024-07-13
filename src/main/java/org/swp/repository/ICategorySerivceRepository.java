package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.swp.entity.ServiceCategory;

import java.util.List;

@Repository
public interface ICategorySerivceRepository extends JpaRepository<ServiceCategory, Integer> {

    @Query(value = "select * from tbl_service_category where is_deleted = false", nativeQuery = true)
    List<ServiceCategory> findAllServiceCategory();
}
