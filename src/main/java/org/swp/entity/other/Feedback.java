package org.swp.entity.other;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.swp.entity.*;
import org.swp.enums.RatingType;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_feedback")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    @NotNull
    private String content;
    @NotNull
    private RatingType ratingType;
    private boolean isEdited = false;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    public Feedback(String content, RatingType ratingType, boolean isEdited, User user, Service service) {
        this.content = content;
        this.ratingType = ratingType;
        this.isEdited = isEdited;
        this.user = user;
        this.service = service;
    }
}
