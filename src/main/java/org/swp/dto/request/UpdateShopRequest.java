package org.swp.dto.request;

import lombok.Data;

import java.time.LocalTime;

@Data
public class UpdateShopRequest {
    private int id;
    private String shopName;
    private String shopAddress;//e.g number 5/3 , 10 streeet
    private String shopPhone;
    private String shopEmail;
    private String area;//Thu Duc City || Sai Gon
    private String shopDescription;
    private String shopTitle;
    private LocalTime openTime;
    private LocalTime closeTime;
    private boolean isAvailable;
    private String shopProfileImangeUrl;
}