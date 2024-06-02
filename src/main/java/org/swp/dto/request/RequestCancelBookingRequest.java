package org.swp.dto.request;

import lombok.Data;

@Data
public class RequestCancelBookingRequest {
    //from both sides to each other (cus -> shop || shop -> cus)
    private int bookingId;
    private String additionalMessage;
}
