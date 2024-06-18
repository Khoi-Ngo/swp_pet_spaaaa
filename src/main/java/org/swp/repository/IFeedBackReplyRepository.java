package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swp.entity.other.FeedbackReply;

public interface IFeedBackReplyRepository extends JpaRepository<FeedbackReply, Integer> {
}
