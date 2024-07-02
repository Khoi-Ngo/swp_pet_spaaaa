package org.swp.dto.request;

import lombok.Data;

@Data
public class FeedbackReplyCreateRequest {
    private int feedbackId;
    private String content;
}
