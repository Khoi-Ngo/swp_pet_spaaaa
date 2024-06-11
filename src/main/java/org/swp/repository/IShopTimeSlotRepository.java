package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.swp.entity.ShopTimeSlot;

import java.time.LocalTime;
import java.util.Set;

@Repository
public interface IShopTimeSlotRepository extends JpaRepository<ShopTimeSlot, Integer> {


    @Query("SELECT sts FROM ShopTimeSlot sts WHERE sts.shop.id = :id")
    Set<ShopTimeSlot> findByShopId(@Param("id") Integer id);

    @Query("SELECT sts FROM ShopTimeSlot sts WHERE sts.shop.id = :id AND sts.startTime = :startLocalTime AND sts.endTime = :endLocalTime")
    ShopTimeSlot findByShopIdAndTimeSlot(@Param("id") Integer id, @Param("startLocalTime") LocalTime startLocalTime, @Param("endLocalTime") LocalTime endLocalTime);
}
