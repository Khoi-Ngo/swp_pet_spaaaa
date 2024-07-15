package org.swp.dto.request;

import lombok.Data;
import org.swp.enums.TypePet;

@Data
public class UpdateServiceRequest {
    private int id;
    private int userId;
    private int serviceCategoryId;
    private String serviceName;
    private String serviceDescription;
    private double price;
    private double minWeight;
    private double maxWeight;
    private String tags;
    private String servicePhoto;
}
