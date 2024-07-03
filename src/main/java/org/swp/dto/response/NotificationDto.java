package org.swp.dto.response;

import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class NotificationDto {
    private Integer id;
    @Lob
    private String content;
    private Integer bookingId;
}
