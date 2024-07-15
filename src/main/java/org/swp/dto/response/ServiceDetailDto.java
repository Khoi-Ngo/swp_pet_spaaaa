package org.swp.dto.response;

import lombok.Data;
import org.swp.enums.TypePet;

@Data
public class ServiceDetailDto {
    private Integer id;
    private String serviceName;
    private String serviceDescription;
    private double price;
    private double minWeight;
    private double maxWeight;
    private String tags; //regex "tag1 - tag2 - tag3"
    private int nomination;
    private String categoryName;
    private Integer shopId;
    private String shopName;
    private String shopAddress;
    private String servicePhoto;
}
