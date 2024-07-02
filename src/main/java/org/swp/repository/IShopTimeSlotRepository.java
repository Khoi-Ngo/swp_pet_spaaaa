package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.swp.entity.ShopTimeSlot;
import org.swp.entity.TimeSlot;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Repository
public interface IShopTimeSlotRepository extends JpaRepository<ShopTimeSlot, Integer> {


    @Query("SELECT sts FROM ShopTimeSlot sts WHERE sts.shop.id = :id")
    List<ShopTimeSlot> findByShopId(@Param("id") Integer id);

    @Query("SELECT sts FROM ShopTimeSlot sts WHERE sts.shop.id = :id AND sts.timeSlot = :timeSlot")
    ShopTimeSlot findByShopIdAndTimeSlot(@Param("id") Integer id, @Param("timeSlot") TimeSlot timeSlot);

}
