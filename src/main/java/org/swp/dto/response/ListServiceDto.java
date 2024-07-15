package org.swp.dto.response;

import lombok.Data;
import org.swp.enums.TypePet;

@Data
public class ListServiceDto {
    private int id;
    private String serviceName;
    private int categoryId;
    private String serviceDescription;
    private double price;
    private double minWeight;
    private double maxWeight;
    private int nomination;
    private String servicePhoto;
}
