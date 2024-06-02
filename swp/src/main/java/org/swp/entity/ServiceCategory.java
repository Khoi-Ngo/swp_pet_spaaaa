package org.swp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tbl_service_category")
public class ServiceCategory extends BaseEntity {
    //this table may be just refered for SERVICE
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String categoryName;
    private String description;



}
