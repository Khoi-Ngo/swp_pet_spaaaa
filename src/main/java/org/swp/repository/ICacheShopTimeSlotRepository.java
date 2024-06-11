package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.swp.entity.CacheShopTimeSlot;
import org.swp.entity.ShopTimeSlot;
import org.swp.entity.TimeSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ICacheShopTimeSlotRepository extends JpaRepository<CacheShopTimeSlot, Integer> {
    @Query("SELECT csts FROM CacheShopTimeSlot csts WHERE csts.shop.id = :id AND csts.localDateTime = :localDateTime")
    List<CacheShopTimeSlot> findByShopIdAndDate(@Param("id") Integer id, @Param("localDateTime") LocalDateTime localDateTime);


    @Query("SELECT csts FROM CacheShopTimeSlot csts WHERE csts.shop.id = :id AND csts.localDateTime = :localDateTime AND csts.shopTimeSlot = :shopTimeSlot")
    CacheShopTimeSlot findByShopDateAndTimeSlot(@Param("id") Integer id, @Param("localDateTime") LocalDateTime localDateTime, @Param("shopTimeSlot") ShopTimeSlot shopTimeSlot);

}
