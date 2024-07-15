package org.swp.dto.response;

import lombok.Data;
import org.swp.enums.NominationType;
@Data
public class NominationListItemDto {
    private int id; //nomination id
    private int shopId;
    private String shopName;
    private NominationType nominationType;
    private String userName;
    private int userId;
    private String userPhoto;
}
