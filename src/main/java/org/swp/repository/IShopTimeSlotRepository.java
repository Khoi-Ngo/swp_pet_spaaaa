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


    @Query("SELECT sts FROM ShopTimeSlot sts WHERE sts.shop.id = :id AND sts.isDeleted = FALSE ")
    List<ShopTimeSlot> findByShopId(@Param("id") Integer id);

    @Query(value = "select *\n" +
            "from tb_shop_time_slot\n" +
            "where shop_id = :id\n" +
            "  and time_slot_id = (select id\n" +
            "                      from tbl_time_slot\n" +
            "                      where start_local_date_time = :start\n" +
            "                        and end_local_date_time = :end\n" +
            "                      limit 1) limit 1\n" +
            ";", nativeQuery = true)
    ShopTimeSlot findByShopIdAndTimeSlot(@Param("id") Integer id, @Param("start") LocalTime start, @Param("end") LocalTime end);

}
