package org.swp.repository;


import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.swp.entity.Booking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface IBookingRepository extends JpaRepository<Booking, Integer> {

    @Query(value = "SELECT * FROM tbl_booking b\n" +
            "WHERE\n" +
            "is_deleted = false AND \n" +
            "customer_id = (SELECT u.id FROM tbl_user u \n" +
            "WHERE\n" +
            "u.username = :userName);", nativeQuery = true)
    List<Booking> findALlByCustomerUserName(@Param("userName") String userName);

    @Query(value = "SELECT * FROM tbl_booking b\n" +
            "WHERE\n" +
            "is_deleted = false AND \n" +
            "shop_id = (SELECT s.id FROM tbl_shop s \n" +
            "WHERE s.shop_owner_id = (SELECT u.id FROM tbl_user u\n" +
            "WHERE u.username = :userName\n" +
            "));", nativeQuery = true)
    List<Booking> findAllByShopOwnerUserName(@Param("userName") String userName);

    @Query(value = "SELECT * FROM tbl_booking WHERE pet_id = :petId AND status = :status ORDER BY id", nativeQuery = true)
    List<Booking> findByPetIdAndStatus(@Param("petId") Integer petId, @Param("status") String status);

    @Query(value = "SELECT * FROM tbl_booking WHERE pet_id = :petId ", nativeQuery = true)
    List<Booking> findByPetId(@Param("petId") Integer petId);

    @Query(value = "SELECT * FROM tbl_booking WHERE shop_id = :shopId ", nativeQuery = true)
    List<Booking> findByShopId(@Param("shopId") Integer shopId);

//    @Query(value = "update\n" +
//            "    tbl_booking\n" +
//            "set is_deleted = 1\n" +
//            "where cache_shop_time_slot_id in\n" +
//            "      (select e.id from tbl_cache_shop_time_slot e where e.shop_time_slot_id = :id);", nativeQuery = true)
//    void deleteAllByShopTimeSlot(@Param("id") int id);

    @Query(value = "SELECT * FROM tbl_booking WHERE pet_id = :petId AND cache_shop_time_slot_id = :cacheId AND status = 'SCHEDULED' AND is_deleted = 0", nativeQuery = true)
    Booking findAnyPetScheduled(@Param("petId") Integer petId, @Param("cacheId") Integer cacheId);

    @Query(value = "SELECT *\n" +
            "FROM tbl_booking b\n" +
            "WHERE is_deleted = false\n" +
            "  AND shop_id = (\n" +
            "    SELECT s.id\n" +
            "    FROM tbl_shop s\n" +
            "    WHERE s.shop_owner_id = (\n" +
            "      SELECT u.id\n" +
            "      FROM tbl_user u\n" +
            "      WHERE u.username = :userName\n" +
            "    )\n" +
            "  ) AND cache_shop_time_slot_id = :cacheShopTimeSlotId;", nativeQuery = true)
    List<Booking> findAllByShopOwnerUserNameAndTimeSlot(@Param("userName") String userName, @Param("cacheShopTimeSlotId") int cacheShopTimeSlotId);


    @Query(value = "SELECT\n" +
            "    DATE_FORMAT(csts.local_date, '%Y-%m') AS month,\n" +
            "    COUNT(*) AS bookings\n" +
            "FROM\n" +
            "    tbl_booking b\n" +
            "JOIN\n" +
            "    tbl_cache_shop_time_slot csts ON b.cache_shop_time_slot_id = csts.id\n" +
            "WHERE\n" +
            "    YEAR(csts.local_date) = YEAR(CURDATE())\n" +
            "    AND b.shop_id = :shopId AND b.is_deleted = false\n" +
            "GROUP BY\n" +
            "    DATE_FORMAT(csts.local_date, '%Y-%m');", nativeQuery = true)
    List<Object[]> findMonthlyBookingsOfShop(@Param("shopId") int shopId);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE) // Locks the selected rows for writing
    @Query("SELECT b FROM Booking b WHERE" +
            " ((b.cacheShopTimeSlot.localDate < :localDate) OR (b.cacheShopTimeSlot.localDate = :localDate and b.cacheShopTimeSlot.shopTimeSlot.timeSlot.endLocalDateTime < :localTime ))" +
            " AND b.status = 'SCHEDULED'")
    List<Booking> findAllScheduledBoookingsAndLock(@Param("localTime") LocalTime localTime, @Param("localDate")LocalDate localDate);
}
