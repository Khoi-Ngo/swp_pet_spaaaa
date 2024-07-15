package org.swp.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingListItemDto {
    private Integer id;
    private String status;
    private String customerFullName;
    private LocalDate localDate;
    private TimeSlotDto timeSlotDto;
    private Integer serviceId;
    private String serviceName;
    private Integer shopId;
    private String shopName;
    private Integer petId;
    private String petName;
    private String servicePhoto;
    private String petPhoto;
}
