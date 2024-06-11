package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.swp.entity.CacheShopTimeSlot;
import org.swp.entity.TimeSlot;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface ICacheShopTimeSlotRepository extends JpaRepository<CacheShopTimeSlot, Integer> {
    @Query("SELECT csts FROM CacheShopTimeSlot csts WHERE csts.shop.id = :id AND csts.localDateTime = :date")
    Set<CacheShopTimeSlot> findByShopIdAndDate(@Param("id") Integer id, @Param("date") LocalDate date);


    @Query("SELECT csts FROM CacheShopTimeSlot csts WHERE csts.shop.id = :id AND csts.localDateTime = :localDate AND csts.shopTimeSlot = :timeSlot")
    CacheShopTimeSlot findByShopDateAndTimeSlot(@Param("id") Integer id, @Param("localDate") LocalDate localDate, @Param("timeSlot") TimeSlot timeSlot);

}
