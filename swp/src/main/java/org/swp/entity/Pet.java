package org.swp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_pet")
@Data
public class Pet extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String petName;
    private String petType;
    private String petAge;
    private String petGender;
    private String petWeight;
    private String petDescription;
    private String petPhoto;
    private String petNote;

    @ManyToMany
    @JoinTable(
            name = "tbl_pet_booking",
            joinColumns = @JoinColumn(name = "pet_id"),
            inverseJoinColumns = @JoinColumn(name = "booking_id")
    )
    private List<Booking> bookingList;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
