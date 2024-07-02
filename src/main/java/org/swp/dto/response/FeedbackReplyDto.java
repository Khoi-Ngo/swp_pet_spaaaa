package org.swp.dto.response;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackReplyDto {
    private String userName;
    @Lob
    private String content;
    private LocalDateTime localDateTime;

}
