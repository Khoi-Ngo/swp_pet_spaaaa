package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.swp.entity.ReferPrice;

import java.util.List;

@Repository
public interface IReferPriceRepository extends JpaRepository<ReferPrice, Integer> {
    @Query(value = "SELECT * FROM tbl_refer_price WHERE service_id = :serviceId AND is_deleted = FALSE", nativeQuery = true)
    List<ReferPrice> findByServiceId(@Param("serviceId") int serviceId);
}
