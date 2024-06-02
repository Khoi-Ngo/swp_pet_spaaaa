package org.swp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_time_slot")
@Data
public class TimeSlot extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime startLocalDateTime;
    private LocalDateTime endLocalDateTime;
}
