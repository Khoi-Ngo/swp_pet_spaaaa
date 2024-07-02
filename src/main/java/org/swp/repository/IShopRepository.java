package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.swp.entity.Pet;
import org.swp.entity.Service;
import org.swp.entity.Shop;
import org.swp.enums.TypePet;

import java.util.List;

@Repository
public interface IShopRepository extends JpaRepository<Shop, Integer> {
    @Query(value = "SELECT * FROM tbl_shop  ORDER BY nomination DESC LIMIT :numberOfRecords", nativeQuery = true)
    List<Service> findMostRcmdShops(@Param("numberOfRecords") int numberOfRecords);

    @Query (value = "SELECT * FROM tbl_shop WHERE shop_owner_id = :shopOwnerId", nativeQuery = true)
    Shop findByShopOwnerId (@Param("shopOwnerId") int shopOwnerId);

    @Query(value = "SELECT * FROM tbl_shop WHERE shop_owner_id = (SELECT id FROM tbl_user WHERE username = :username) AND is_deleted = FALSE", nativeQuery = true)
    Shop findByUserName(@Param("username") String username);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tbl_service SET is_deleted = 1 WHERE shop_id = :id", nativeQuery = true)
    void updateServiceDeletedByShopId(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tbl_nomination SET is_deleted = 1 WHERE shop_id = :id", nativeQuery = true)
    void updateNominationDeletedByShopId(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tbl_cache_shop_time_slot SET is_deleted = 1 WHERE shop_id = :id", nativeQuery = true)
    void updateCacheShopTimeSlotDeletedByShopId(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tbl_booking SET is_deleted = 1 WHERE shop_id = :id", nativeQuery = true)
    void updateBookingDeletedByShopId(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tb_shop_time_slot SET is_deleted = 1 WHERE shop_id = :id", nativeQuery = true)
    void updateShopTimeSlotDeletedByShopId(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tbl_feedback SET is_deleted = 1 WHERE shop_id = :id", nativeQuery = true)
    void updateFeedBackByShopId(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tbl_feedback_reply SET is_deleted = 1 WHERE feedback_id IN (SELECT id FROM tbl_feedback WHERE shop_id = :shopId)", nativeQuery = true)
    void updateFeedbackReplyDeletedByShopId(@Param("shopId") int shopId);


}
