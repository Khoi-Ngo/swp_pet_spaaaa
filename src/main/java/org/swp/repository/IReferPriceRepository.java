package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.swp.entity.ReferPrice;

import java.util.List;

@Repository
public interface IReferPriceRepository extends JpaRepository<ReferPrice, Integer> {
    @Query(value = "SELECT * FROM tbl_refer_price WHERE service_id = :serviceId and is_deleted = false", nativeQuery = true)
    List<ReferPrice> findByServiceId(@Param("serviceId") int serviceId);

    @Modifying
    @Transactional
    @Query(value = "update tbl_refer_price set is_deleted = true where service_id = :id", nativeQuery = true)
    void deleteAllByServiceId(@Param("id") int id);
}
