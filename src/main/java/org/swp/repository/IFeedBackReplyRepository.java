package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.swp.entity.other.Feedback;
import org.swp.entity.other.FeedbackReply;

import java.util.Collection;
import java.util.List;

public interface IFeedBackReplyRepository extends JpaRepository<FeedbackReply, Integer> {
    @Query(value = "SELECT * FROM tbl_feedback_reply WHERE feedback_id = :feedbackId ORDER BY id DESC LIMIT :numberOfRecords", nativeQuery = true)
    List<FeedbackReply> findFeedbackRe(@Param("feedbackId") int feedbackId, @Param("numberOfRecords") int numberOfRecords);}
