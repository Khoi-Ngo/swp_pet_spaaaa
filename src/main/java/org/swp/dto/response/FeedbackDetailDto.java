package org.swp.dto.response;

import jakarta.persistence.Lob;
import lombok.Data;
import org.swp.enums.RatingType;

import java.time.LocalDateTime;

@Data
public class FeedbackDetailDto {
    private Integer id;
    private String userName;
    @Lob
    private String content;
    private RatingType ratingType;
    private boolean isEdited;
    private LocalDateTime localDateTime;
}
