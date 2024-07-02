package org.swp.dto.response;

import lombok.Data;

import java.time.LocalTime;

@Data
public class ListTimeSlotDto {
    private Integer id;
    private LocalTime startLocalDateTime;
    private LocalTime endLocalDateTime;
}
