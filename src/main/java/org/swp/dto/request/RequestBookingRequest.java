package org.swp.dto.request;

import lombok.Data;
import org.swp.dto.response.CacheShopTimeSlotDto;
import org.swp.dto.response.TimeSlotDto;
import org.swp.entity.TimeSlot;

import java.time.LocalDate;

@Data
public class RequestBookingRequest {

    private int customerId;
    private String customerName;
    private String customerAddress;
    private String customerPhone;
    private String customerEmail;
    private String additionalMessage;//from customer to shop
    private int serviceId;
    private String serviceName;

    //date - timeslot
    private LocalDate localDate;
    private TimeSlotDto timeSlotDto;


}
