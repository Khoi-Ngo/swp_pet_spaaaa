package org.swp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.swp.entity.TimeSlot;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CacheShopTimeSlotDto {

    private int totalSlots;
    private int usedSlots;
    private int availableSlots;
    private TimeSlotDto timeSlotDto;
}
