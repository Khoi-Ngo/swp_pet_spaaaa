package org.swp.dto.response;

import lombok.Data;
import org.swp.enums.RatingType;

import java.time.LocalDateTime;

@Data
public class FeedbackListItemDto {
    private Integer id;
    private String userName;
    private String content;
    private RatingType ratingType;
    private boolean isEdited;
    private LocalDateTime localDateTime;
    private int userId;
    private String userPhoto;
}
