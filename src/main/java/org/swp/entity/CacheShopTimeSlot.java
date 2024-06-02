package org.swp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_cache_shop_time_slot")
@Data
public class CacheShopTimeSlot extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int totalSlots;
    private int usedSlots;
    private int availableSlots;

    private LocalDateTime localDateTime;

    @ManyToOne
    @JoinColumn(name = "shop_time_slot_id")
    private ShopTimeSlot shopTimeSlot;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToMany
    @JoinTable(name = "tbl_cache_shop_time_slot_booking")
    private List<Booking> bookings;
}
