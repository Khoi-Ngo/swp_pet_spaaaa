package org.swp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReferPriceCreateRequest {
    //weight range
    private double minWeight;
    private double maxWeight;
    //price
    @NotNull
    private double referPrice;
    private Integer serviceId;
    private String petType;
}
