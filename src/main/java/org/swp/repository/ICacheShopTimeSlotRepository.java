package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.swp.entity.CacheShopTimeSlot;
import org.swp.entity.ShopTimeSlot;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ICacheShopTimeSlotRepository extends JpaRepository<CacheShopTimeSlot, Integer> {
    @Query("SELECT csts FROM CacheShopTimeSlot csts WHERE csts.shop.id = :id AND csts.localDate = :localDate")
    List<CacheShopTimeSlot> findByShopIdAndDate(@Param("id") Integer id, @Param("localDate") LocalDate localDate);


    @Query("SELECT csts FROM CacheShopTimeSlot csts WHERE csts.shop.id = :id AND csts.localDate = :localDate AND csts.shopTimeSlot = :shopTimeSlot")
    CacheShopTimeSlot findByShopDateAndTimeSlot(@Param("id") Integer id, @Param("localDate") LocalDate localDate, @Param("shopTimeSlot") ShopTimeSlot shopTimeSlot);


    @Query("SELECT csts FROM CacheShopTimeSlot csts WHERE csts.shop.id = :shopId AND csts.shopTimeSlot.id = :shopTimeSlotId")
    CacheShopTimeSlot findByShopIdShopTimeSlotId(@Param("shopId") int shopId, @Param("shopTimeSlotId") int shopTimeSlotId);
}
