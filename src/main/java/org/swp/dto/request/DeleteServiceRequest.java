package org.swp.dto.request;

import lombok.Data;

@Data
public class DeleteServiceRequest {
    private int userId;
    private int serviceId;
}
