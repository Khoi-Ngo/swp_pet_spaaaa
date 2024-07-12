package org.swp.dto.response;

import lombok.Data;
import org.swp.entity.Shop;
import org.swp.enums.UserRole;


@Data
public class ListAccountShopOwnerDto {
    private Integer id;
    private String username;
    private String email;
    private String phone;
    private boolean isDeleted;
}
