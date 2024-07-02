package org.swp.entity.other;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.swp.entity.BaseEntity;
import org.swp.entity.Service;
import org.swp.entity.Shop;
import org.swp.entity.User;
import org.swp.enums.NominationType;

@Entity
@Table(name = "tbl_nomination")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Nomination extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    private NominationType nominationType;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    public Nomination(User user, NominationType nominationType) {
        this.user = user;
        this.nominationType = nominationType;
    }
}
