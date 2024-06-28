package org.swp.dto.response;

import lombok.Data;

import java.time.LocalTime;

@Data
public class ListShopTimeSlotDto {
    private Integer id;
    private LocalTime startLocalTime;
    private LocalTime endLocalTime;
    private String description;
    private int totalSlot;
    private int availableSlots;
    private int usedSlots;
    private boolean status;
}
