package org.swp.dto.request;

import jakarta.persistence.Lob;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserProfileRequest {
    private Integer id;
//    private String username;
    private String firstName;
    private String lastName;
    private String email;//todo: need to validate duplicate
    private String phone;
    @Lob
    private String profileImageUrl;
    @Lob
    private String coverImageUrl;
    private LocalDate birthday;
}
