package org.swp.dto.request;

import lombok.Data;

@Data
public class FeedbackReplyRequest {
    private int feedbackId;
    private String content;
}
