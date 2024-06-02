package org.swp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_shop_time_slot")
@Data
public class ShopTimeSlot extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Lob
    private String description;


    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToOne
    @JoinColumn(name = "time_slot_id")
    private TimeSlot timeSlot;
}
