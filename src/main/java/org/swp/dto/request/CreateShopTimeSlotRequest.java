package org.swp.dto.request;

import jakarta.persistence.*;
import lombok.Data;
import org.swp.entity.Shop;
import org.swp.entity.TimeSlot;

@Data
public class CreateShopTimeSlotRequest {
    private int shopId;
    private int timeSlotId;
    private String description;
    private int totalSlot;

}
