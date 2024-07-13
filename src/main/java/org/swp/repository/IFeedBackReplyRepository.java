package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.swp.entity.other.Feedback;
import org.swp.entity.other.FeedbackReply;

import java.util.Collection;
import java.util.List;

public interface IFeedBackReplyRepository extends JpaRepository<FeedbackReply, Integer> {
    @Query(value = "SELECT * FROM tbl_feedback_reply WHERE feedback_id = :feedbackId  AND is_deleted = FALSE ORDER BY id DESC LIMIT :numberOfRecords", nativeQuery = true)
    List<FeedbackReply> findByFeedbackId(@Param("feedbackId") int feedbackId, @Param("numberOfRecords") int numberOfRecords);

    @Query(value = "SELECT * FROM tbl_feedback_reply WHERE feedback_id = :feedbackId AND is_deleted = FALSE", nativeQuery = true)
    List<FeedbackReply> findByFeedbackId(@Param("feedbackId") int feedbackId);

    @Modifying
    @Transactional
    @Query(value = "update tbl_feedback_reply set is_deleted = true where feedback_id = :id", nativeQuery = true)
    void deletedAllByFeedBackId(@Param("id") int id);
}
