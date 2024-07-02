package org.swp.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReferPriceDto {
    private Integer id;
    //weight range
    private double minWeight;
    private double maxWeight;
    //price
    @NotNull
    private double referPrice;
}
