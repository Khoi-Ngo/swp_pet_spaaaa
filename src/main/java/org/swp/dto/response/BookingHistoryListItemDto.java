package org.swp.dto.response;

import lombok.Data;
import lombok.Setter;

@Data
public class BookingHistoryListItemDto {
    private Integer id;
    private String status;
    private int serviceId;
    private String serviceName;
    private int shopId;
    private String shopName;
}
