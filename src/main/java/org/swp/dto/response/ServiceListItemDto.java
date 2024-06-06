package org.swp.dto.response;

import lombok.Data;
import org.swp.enums.TypePet;

@Data
public class ServiceListItemDto {
    private Integer id;
    private String serviceName;
    private double price;
    private TypePet typePet;
    private String typePetString;
    private int nomination;
}
