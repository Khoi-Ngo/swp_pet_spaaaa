package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.swp.entity.Notification;

import java.util.List;

@Repository
public interface INotificationRepository extends JpaRepository<Notification, Integer> {
    @Query(value = "SELECT * FROM tbl_notification where user_id = (SELECT id FROM tbl_user WHERE username = :username) and is_deleted = false and is_read = false ORDER BY id DESC", nativeQuery = true)
    List<Notification> findAllByUser(@Param("username") String username);
    @Query(value = "SELECT count(id) FROM tbl_notification where user_id = (SELECT id FROM tbl_user WHERE username = :username) and is_deleted = false and is_read = false ORDER BY id DESC", nativeQuery = true)
    Integer getTotalUnread(@Param("username") String username);
}
