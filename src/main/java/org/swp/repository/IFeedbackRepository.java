package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.swp.entity.Service;
import org.swp.entity.other.Feedback;

import java.util.Collection;
import java.util.List;

public interface IFeedbackRepository extends JpaRepository<Feedback, Integer> {

    @Query(value = "SELECT * FROM tbl_feedback WHERE service_id = :serviceId AND is_deleted = FALSE ORDER BY id DESC LIMIT :numberOfRecords", nativeQuery = true)
    List<Feedback> findLatestFeedbackByServiceId(@Param("serviceId") int serviceId, @Param("numberOfRecords") int numberOfRecords);

    @Query(value = "SELECT * FROM tbl_feedback WHERE service_id = :serviceId AND is_deleted = FALSE ORDER BY id DESC", nativeQuery = true)
    List<Feedback> findAllFeedbackByServiceId(@Param("serviceId") int serviceId);

    @Modifying
    @Transactional
    @Query(value = "update tbl_feedback set is_deleted = true where user_id = :id", nativeQuery = true)
    void deletedAllFeedBackByUserId(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "update tbl_feedback set is_deleted = true where service_id = :id", nativeQuery = true)
    void deletedAllFeedBackByServiceId(@Param("id") int id);

}

