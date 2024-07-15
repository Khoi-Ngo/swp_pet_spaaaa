package org.swp.dto.request;

import lombok.Data;
import org.swp.enums.TypePet;

@Data
public class CreateServiceRequest {
    private int userId;
    private int serviceCategoryId;
    private String serviceName;
    private String serviceDescription;
    private double price;
    private double minWeight;
    private double maxWeight;
    private TypePet typePet;
    private String tags;
    private String servicePhoto;
}
