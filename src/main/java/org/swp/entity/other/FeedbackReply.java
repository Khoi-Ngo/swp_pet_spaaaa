package org.swp.entity.other;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.swp.entity.BaseEntity;
import org.swp.entity.User;

@Entity
@Table(name = "tbl_feedback_reply")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackReply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    private String content;
    private boolean isEdited = false;
    //relation with user and the feedback which is replied
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    public FeedbackReply(String content, User user, Feedback feedback) {
        this.content = content;
        this.user = user;
        this.feedback = feedback;
    }
}
