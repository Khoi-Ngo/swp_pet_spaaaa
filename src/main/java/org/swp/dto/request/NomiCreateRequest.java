package org.swp.dto.request;

import lombok.Data;
import org.swp.enums.NominationType;

@Data
public class NomiCreateRequest {
    private Integer shopId;
    private NominationType nominationType;
}
