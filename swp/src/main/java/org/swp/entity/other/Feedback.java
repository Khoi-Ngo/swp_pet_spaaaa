package org.swp.entity.other;

import jakarta.persistence.*;
import lombok.Data;
import org.swp.entity.Service;
import org.swp.entity.ServiceCategory;
import org.swp.entity.Shop;
import org.swp.entity.User;

@Entity
@Table(name = "tbl_feedback")
@Data
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    private String content;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //feedback -> service + shop
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;
}
