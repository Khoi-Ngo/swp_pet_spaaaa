package org.swp.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.swp.enums.TypePet;

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
    private TypePet petType;
    private int petAge;
    private String petGender;
    private int petWeight;
    private String petDescription;
    private String petPhoto;
    private String petNote;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



}
