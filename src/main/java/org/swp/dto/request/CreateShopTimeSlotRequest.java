package org.swp.dto.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.swp.entity.Shop;
import org.swp.entity.TimeSlot;

@Data
public class CreateShopTimeSlotRequest {
    @NotNull(message = "Shop ID cannot be null")
    @Min(value = 0, message = "Shop ID must be greater than or equal to 0")
    private int shopId;

    @NotNull(message = "Time Slot ID cannot be null")
    private int timeSlotId;

    private String description;

    @Min(value = 0, message = "Total Slot must be greater than or equal to 0")
    private int totalSlot;

}
