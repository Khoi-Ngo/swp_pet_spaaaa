package org.swp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReferPriceUpdateRequest {
    private Integer id;
    //weight range
    private Double minWeight;
    private Double maxWeight;
    //price
    @NotNull
    private Double referPrice;
}
