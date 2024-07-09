package org.swp.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "tbl_booking")
public class Booking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Lob
    private String bookingNote;
    private String status;
    private String additionalMessage;// -> used for cancel booking only


    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;
    @ManyToOne
    @JoinColumn(name = "cache_shop_time_slot_id")
    private CacheShopTimeSlot cacheShopTimeSlot;
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;
}
