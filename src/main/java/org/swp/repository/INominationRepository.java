package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.swp.entity.other.Nomination;

import java.util.List;

public interface INominationRepository extends JpaRepository<Nomination, Integer> {
    @Query(value = "SELECT * FROM tbl_nomination WHERE user_id = :userId AND is_deleted = false", nativeQuery = true)
    List<Nomination> findAllByUserId(@Param("userId") int userId);

    @Query(value = "SELECT * FROM tbl_nomination WHERE shop_id = :shopId AND user_id = :userId LIMIT 1", nativeQuery = true)
    Nomination findByShopIdAndUserId(@Param("shopId") int shopId, @Param("userId") int userId);

    @Query(value = "SELECT * FROM tbl_nomination WHERE shop_id = :shopId AND is_deleted = false", nativeQuery = true)
    List<Nomination> findAllByShopId(@Param("shopId") int shopId);
}
