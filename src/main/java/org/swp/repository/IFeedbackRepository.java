package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.swp.entity.Service;
import org.swp.entity.other.Feedback;

import java.util.List;

public interface IFeedbackRepository extends JpaRepository<Feedback, Integer> {

    @Query(value = "SELECT * FROM tbl_feedback WHERE service_id = :serviceId ORDER BY id DESC LIMIT :numberOfRecords", nativeQuery = true)
    List<Feedback> findLatestFeedbackByServiceId(@Param("serviceId") int serviceId, @Param("numberOfRecords") int numberOfRecords);
}

