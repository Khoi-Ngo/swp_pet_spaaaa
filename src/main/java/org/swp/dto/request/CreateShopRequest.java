package org.swp.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class CreateShopRequest {
    private int userId;
    private String shopName;
    private String shopAddress;//e.g number 5/3 , 10 streeet
    private String shopPhone;
    private String shopEmail;
    private String area;//Thu Duc City || Sai Gon
    private String shopDescription;
    private LocalTime openTime;
    private LocalTime closeTime;
    private boolean isAvailable;
    private String shopTitle;
    private String shopProfileImangeUrl;
}
