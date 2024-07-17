package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.swp.entity.User;
import java.util.List;

import java.util.Collection;

@Repository
public interface IAdminRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT * FROM tbl_user WHERE role = 1 AND is_deleted = false ", nativeQuery = true)
    List<User> findAllCustomerAcc();

    @Query(value = "SELECT * FROM tbl_user WHERE role = 2 AND is_deleted = false ", nativeQuery = true)
    List<User> findAllShopOwnerAcc();

    @Query(value = "select count(*) from tbl_shop where is_deleted = false", nativeQuery = true)
    int countTotalShops();

    @Query(value = "select count(*) from tbl_service where is_deleted = false", nativeQuery = true)
    int countTotalServices();

    @Query(value = "select count(*) from tbl_user where is_deleted = false and role = 1", nativeQuery = true)
    int countTotalCustomer();

    @Query(value = "select count(*) from tbl_booking where is_deleted = false", nativeQuery = true)
    int countTotalBookings();

    @Query(value = "select count(*) from tbl_pet where is_deleted = false", nativeQuery = true)
    int countTotalPets();

    @Query(value = "select count(*) from tbl_user where is_deleted = false and role = 2", nativeQuery = true)
    int countTotalShopOwners();

    @Query(value = "SELECT\n" +
            "    DATE_FORMAT(csts.local_date, '%Y-%m') AS month,\n" +
            "    COUNT(*) AS bookings\n" +
            "FROM\n" +
            "    tbl_booking b\n" +
            "JOIN\n" +
            "    tbl_cache_shop_time_slot csts ON b.cache_shop_time_slot_id = csts.id\n" +
            "WHERE\n" +
            "    YEAR(csts.local_date) = YEAR(CURDATE()) AND b.is_deleted = false\n" +
            "GROUP BY\n" +
            "    DATE_FORMAT(csts.local_date, '%Y-%m');", nativeQuery = true)
    List<Object[]> findMonthlyBookings();

}
