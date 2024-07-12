package org.swp.entity.other;

import jakarta.persistence.*;
import lombok.Data;
import org.swp.entity.BaseEntity;
import org.swp.entity.User;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tbl_password_reset_token")
public class PasswordResetToken{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String token;
    private LocalDateTime expiryDateTime;
    @OneToOne(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

}
