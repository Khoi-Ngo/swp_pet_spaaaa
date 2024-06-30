package org.swp.dto.request;

import lombok.Data;

@Data
public class NominationRequest {
    private int serviceId;
    private int shopId;
    private Integer rating;
}
