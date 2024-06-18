package org.swp.dto.response;

import lombok.Data;

@Data
public class BookingHistoryListItemDto {
    private Integer id;
    private String status;
    private String serviceName;
}
