package org.swp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_cache_shop_time_slot")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CacheShopTimeSlot extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int totalSlots;
    private int usedSlots;
    private int availableSlots;

    private LocalDate localDate; //date

    @ManyToOne
    @JoinColumn(name = "shop_time_slot_id")
    private ShopTimeSlot shopTimeSlot;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    public CacheShopTimeSlot(int totalSlots, int usedSlots, int availableSlots, LocalDate localDate,
                             ShopTimeSlot shopTimeSlot, Shop shop) {
        this.totalSlots = totalSlots;
        this.usedSlots = usedSlots;
        this.availableSlots = availableSlots;
        this.localDate = localDate;
        this.shopTimeSlot = shopTimeSlot;
        this.shop = shop;
    }
}
