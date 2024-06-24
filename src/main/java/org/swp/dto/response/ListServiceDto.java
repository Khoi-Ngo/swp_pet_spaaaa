package org.swp.dto.response;

import lombok.Data;
import org.swp.enums.TypePet;

@Data
public class ListServiceDto {
    private String serviceName;
    private String serviceDescription;
    private double price;
    private double minWeight;
    private double maxWeight;
    private String tags;
    private int nomination;
}
