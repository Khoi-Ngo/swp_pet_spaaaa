package org.swp.dto.request;

import lombok.Data;
import org.swp.enums.RatingType;

@Data
public class FeedbackCreateRequest {
    private String content;
    private RatingType ratingType;
    private Integer serviceId;
}
