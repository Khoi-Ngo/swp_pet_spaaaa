package org.swp.dto.request;

import lombok.Data;

import java.time.LocalTime;

@Data
public class CreateTimeSlotRequest {
    private LocalTime startLocalDateTime;
    private LocalTime endLocalDateTime;
}
