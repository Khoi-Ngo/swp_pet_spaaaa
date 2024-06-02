package org.swp.entity.other;

import jakarta.persistence.*;
import lombok.Data;
import org.swp.entity.User;

@Entity
@Table(name = "tbl_feedback_reply")
@Data
public class FeedbackReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    private String content;

    //relation with user and the feedback which is replied
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

}
