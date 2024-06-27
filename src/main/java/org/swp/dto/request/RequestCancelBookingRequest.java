package org.swp.dto.request;

import lombok.Data;

@Data
public class RequestCancelBookingRequest {
    private int bookingId;
    private String additionalMessage;
}
