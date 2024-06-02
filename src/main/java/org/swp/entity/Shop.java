package org.swp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "tbl_shop")
public class Shop extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    private String shopName;
    private String shopAddress;//e.g number 5/3 , 10 streeet
    private String shopPhone;
    private String shopEmail;
    private String area;//Thu Duc City || Sai Gon
    @Lob
    private String shopDescription;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private boolean isAvailable;
    //day of week working
    private String shopTitle;
    @Lob
    private String shopProfileImangeUrl;
    @Lob
    private String shopCoverImageUrl;
    private int totalServices;
    private int nomination;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shop_owner_id", referencedColumnName = "id")
    private User user;

}
