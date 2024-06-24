package org.swp.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateShopRequest {
    private int userId;
    private String shopName;
    private String shopAddress;//e.g number 5/3 , 10 streeet
    private String shopPhone;
    private String shopEmail;
    private String area;//Thu Duc City || Sai Gon
    private String shopDescription;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private boolean isAvailable;
    private String shopTitle;
}
