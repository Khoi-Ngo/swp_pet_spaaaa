package org.swp.dto.request;

import lombok.Data;
import org.swp.enums.NominationType;

@Data
public class NominationDeleteRequest {
    private Integer shopId;
    private NominationType nominationType;
}
