package org.swp.dto.request;

import jakarta.persistence.*;
import lombok.Data;
import org.swp.entity.Booking;
import org.swp.entity.Shop;
import org.swp.entity.ShopTimeSlot;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateShopTimeSlotRequest {
    private Integer id;
    private String description;
    private int totalSlot;
    private int shopId;
    private int timeSlotId;
    private Integer userId;
}
