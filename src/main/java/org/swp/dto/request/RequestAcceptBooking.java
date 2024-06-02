package org.swp.dto.request;

import lombok.Data;

@Data
public class RequestAcceptBooking {//used for customer/shop-owner accept changing  of status or other stuffs of booking (e.g: date, number, ...)
    private int bookingId;
    private String additionalMessage;
}
