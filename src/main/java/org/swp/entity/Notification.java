package org.swp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_notification")
@Data
public class Notification extends BaseEntity {
    //This notification is currently used for Booking feature only
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Lob
    private String content;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    //if extend scope -> can have more relationship with other entities such as: pet, service, shop, ...
}
