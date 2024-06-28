package org.swp.dto.request;

import lombok.Data;

import java.time.LocalTime;

@Data
public class UpdateTimeSlotRequest {
    private Integer id;
    private LocalTime startLocalDateTime;
    private LocalTime endLocalDateTime;
}
