package org.swp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "tbl_refer_price")
public class ReferPrice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //weight range
    private double minWeight;
    private double maxWeight;
    //price
    @NotNull
    private double referPrice;
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;
    private String petType;

}
