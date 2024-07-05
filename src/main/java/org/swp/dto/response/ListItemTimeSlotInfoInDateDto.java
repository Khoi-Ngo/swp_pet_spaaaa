package org.swp.dto.response;

import lombok.Data;

import java.time.LocalTime;

@Data
public class ListItemTimeSlotInfoInDateDto {
    private int id;//cacheTimeSlotId
    private int totalSlots;
    private int usedSlots;
    private int availableSlots;
    private LocalTime startTime;
    private LocalTime endTime;
}
