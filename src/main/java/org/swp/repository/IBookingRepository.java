package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.swp.entity.Booking;

import java.util.List;

@Repository
public interface IBookingRepository extends JpaRepository<Booking, Integer> {

    @Query(value = "SELECT * FROM tbl_booking b\n" +
            "WHERE\n" +
            "customer_id = (SELECT u.id FROM tbl_user u \n" +
            "WHERE\n" +
            "u.username = :userName);", nativeQuery = true)
    List<Booking> findALlByCustomerUserName(@Param("userName") String userName);

    @Query(value = "SELECT * FROM tbl_booking b\n" +
            "WHERE\n" +
            "shop_id = (SELECT s.id FROM tbl_shop s \n" +
            "WHERE s.shop_owner_id = (SELECT u.id FROM tbl_user u\n" +
            "WHERE u.username = :userName\n" +
            "));", nativeQuery = true)
    List<Booking> findAllByShopOwnerUserName(@Param("userName") String userName);

    @Query(value = "SELECT * FROM tbl_booking WHERE pet_id = :petId AND status = :status", nativeQuery = true)
    List<Booking> findByPetIdAndStatus(@Param("petId") Integer petId, @Param("status") String status);

    @Query(value = "SELECT * FROM tbl_booking WHERE pet_id = :petId ", nativeQuery = true)
    List<Booking> findByPetId(@Param("petId") Integer petId);

    @Query(value = "SELECT * FROM tbl_booking WHERE shop_id = :shopId ", nativeQuery = true)
    List<Booking> findByShopId(@Param("shopId") Integer shopId);
}
