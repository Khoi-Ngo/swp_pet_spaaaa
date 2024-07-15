package org.swp.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.swp.enums.TypePet;


@Entity
@Data
@Table(name = "tbl_service")
public class Service extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String serviceName;
    private String serviceDescription;
    private double price;
    private double minWeight;
    private double maxWeight;
    private String tags; //regex "tag1 - tag2 - tag3"
    private int nomination;
    @Lob
    private String servicePhoto;

    @ManyToOne
    @JoinColumn(name = "service_category_id")
    private ServiceCategory category;
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;


}
