package org.swp.dto.response;

import lombok.Data;
import org.swp.enums.NominationType;

@Data
public class NominationServiceListItemDto {
    private int serviceId;
    private String serviceName;
    private String shopName;
    private NominationType nominationType;
}
