package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.swp.entity.Booking;

@Repository
public interface IBookingRepository extends JpaRepository<Booking, Integer> {

    @Query(value = "SELECT b FROM tbl_booking b\n" +
            "WHERE\n" +
            "b.customer_id = (SELECT u.id FROM tbl_user u \n" +
            "WHERE\n" +
            "u.username = :userName);", nativeQuery = true)
    Object findALlByCustomerUserName(@Param("userName") String userName);

    @Query(value = "SELECT b FROM tbl_booking b\n" +
            "WHERE\n" +
            "shop_id = (SELECT s.id FROM tbl_shop s \n" +
            "WHERE s.shop_owner_id = (SELECT u.id FROM tbl_user u\n" +
            "WHERE u.username = :userName\n" +
            "));", nativeQuery = true)
    Object findAllByShopOwnerUserName(@Param("userName") String userName);
}
