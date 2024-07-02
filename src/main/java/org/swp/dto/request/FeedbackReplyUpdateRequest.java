package org.swp.dto.request;

import lombok.Data;

@Data
public class FeedbackReplyUpdateRequest {
    private Integer feedbackReplyId;
    private String updateContent;
}
