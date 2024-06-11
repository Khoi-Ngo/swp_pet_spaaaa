package org.swp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.swp.entity.TimeSlot;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CacheShopTimeSlotDto {
    private Integer shopId;
    private int totalSlots;
    private int usedSlots;
    private int availableSlots;
    private LocalDateTime localDateTime;
    private TimeSlotDto timeSlotDto;
}
