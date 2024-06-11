package org.swp.dto.response;

import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeSlotDto {
    private LocalTime startLocalDateTime;
    private LocalTime endLocalDateTime;
}
