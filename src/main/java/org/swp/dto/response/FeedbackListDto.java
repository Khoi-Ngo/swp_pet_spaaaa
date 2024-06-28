package org.swp.dto.response;

import lombok.Data;

@Data
public class FeedbackListDto {
    private Integer id;
    private String content;
    private String userName;
    private String serviceName;
    private String shopName;
}
