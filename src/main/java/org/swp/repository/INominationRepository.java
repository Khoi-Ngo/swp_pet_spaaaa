package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.swp.entity.other.Nomination;

import java.util.List;

public interface INominationRepository extends JpaRepository<Nomination, Integer> {

    @Query(value = "SELECT * FROM tbl_nomination WHERE service_id = :serviceId ORDER BY id DESC", nativeQuery = true)
    List<Nomination> findAllByServiceId(@Param("serviceId") int serviceId);

    @Query(value = "SELECT * FROM tbl_nomination WHERE shop_id = :shopId ORDER BY id DESC", nativeQuery = true)
    List<Nomination> findAllByShopId(@Param("shopId") int shopId);
}


