package org.swp.dto.request;

import lombok.Data;

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

}
