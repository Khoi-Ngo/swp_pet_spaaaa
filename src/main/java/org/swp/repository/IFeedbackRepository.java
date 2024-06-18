package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swp.entity.other.Feedback;

public interface IFeedbackRepository extends JpaRepository<Feedback, Integer> {

}
