package org.swp.dto.response;

import lombok.Data;
import org.swp.enums.NominationType;
@Data
public class NominationListItemDto {
    private int shopId;
    private String shopName;
    private NominationType nominationType;
}
