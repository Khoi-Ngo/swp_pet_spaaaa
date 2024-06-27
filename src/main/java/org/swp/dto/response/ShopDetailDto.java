package org.swp.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Lob;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    private boolean isAvailable;
    //day of week working
    private String shopTitle;
    @Lob
    private String shopProfileImangeUrl;
    @Lob
    private String shopCoverImageUrl;
    private int totalServices;
    private int nomination;

    //booking detail
    private Map<LocalDateTime, List<BookingHistoryListItemDto>> bookingHistory = new HashMap<>();

}
