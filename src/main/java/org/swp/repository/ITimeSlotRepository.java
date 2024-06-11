package org.swp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.swp.entity.TimeSlot;

import java.time.LocalTime;

@Repository
public interface ITimeSlotRepository extends JpaRepository<TimeSlot, Integer> {
    @Query("SELECT t FROM TimeSlot t WHERE t.startLocalDateTime = ?1 AND t.endLocalDateTime = ?2")
    TimeSlot findByStartAndEnd(LocalTime startLocalDateTime, LocalTime endLocalDateTime);
}
