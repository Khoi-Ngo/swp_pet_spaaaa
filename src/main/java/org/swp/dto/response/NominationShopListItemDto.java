package org.swp.dto.response;

import lombok.Data;
import org.swp.enums.NominationType;
@Data
public class NominationShopListItemDto {
    private int shopId;
    private String shopName;
    private NominationType nominationType;
}
