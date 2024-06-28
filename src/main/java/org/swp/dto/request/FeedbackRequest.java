package org.swp.dto.request;

import lombok.Data;

@Data
public class FeedbackRequest {
    private int serviceId;
    private String content;
    private int shopId;
}
