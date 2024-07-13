package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.swp.entity.other.Nomination;

import java.util.List;

public interface INominationRepository extends JpaRepository<Nomination, Integer> {
    @Query(value = "SELECT * FROM tbl_nomination WHERE user_id = :userId AND is_deleted = false", nativeQuery = true)
    List<Nomination> findAllByUserId(@Param("userId") int userId);

    @Query(value = "SELECT * FROM tbl_nomination WHERE shop_id = :shopId AND user_id = :userId and is_deleted = false LIMIT 1", nativeQuery = true)
    Nomination findByShopIdAndUserId(@Param("shopId") int shopId, @Param("userId") int userId);

    @Query(value = "SELECT * FROM tbl_nomination WHERE shop_id = :shopId AND is_deleted = false", nativeQuery = true)
    List<Nomination> findAllByShopId(@Param("shopId") int shopId);

    @Modifying
    @Transactional
    @Query(value = "update tbl_nomination set is_deleted = true where user_id = :id", nativeQuery = true)
    void deletedAllNominationByUserId(@Param("id") int id);
}
