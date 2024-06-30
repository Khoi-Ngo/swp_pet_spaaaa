package org.swp.dto.response;

import lombok.Data;

@Data
public class NominationDto {
    private Integer id; //nomination id
    private String serviceName;
    private String shopName;
    private Integer rating;
    private String userName;
}
