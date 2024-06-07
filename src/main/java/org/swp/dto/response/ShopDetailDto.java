package org.swp.dto.response;

import jakarta.persistence.Lob;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShopDetailDto {
    //this is the action user click or search Specified a Shop
    private Integer id;
    private String shopName;
    private String shopAddress;//e.g number 5/3 , 10 streeet
    private String shopPhone;
    private String shopEmail;
    private String area;//Thu Duc City || Sai Gon
    @Lob
    private String shopDescription;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private boolean isAvailable;
    //day of week working
    private String shopTitle;
    @Lob
    private String shopProfileImangeUrl;
    @Lob
    private String shopCoverImageUrl;
    private int totalServices;
    private int nomination;

}
