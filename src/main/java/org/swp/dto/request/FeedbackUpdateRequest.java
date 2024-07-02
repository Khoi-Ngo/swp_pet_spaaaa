package org.swp.dto.request;

import lombok.Data;
import org.swp.enums.RatingType;

@Data
public class FeedbackUpdateRequest {
    private Integer feedbackId;
    private String updateContent;
    private RatingType updateRatingType;
}
