package org.swp.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.swp.enums.BookingStatus;

import java.util.List;

@Entity
@Data
@Table(name = "tbl_booking")
public class Booking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Lob
    private String bookingNote;
    private boolean isDone;
    private boolean isCanceled;
    private String status;


    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;
    @ManyToMany
    @JoinTable(
            name = "tbl_booking_time",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "cache_shop_time_slot")
    )
    private List<CacheShopTimeSlot> cacheShopTimeSlotList;
}
