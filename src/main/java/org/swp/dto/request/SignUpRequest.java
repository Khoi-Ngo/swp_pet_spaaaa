package org.swp.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.swp.enums.UserRole;

@Data
public class SignUpRequest {
    //todo adding validation + removing UserRole
    private String firstName;
    private String lastName;
    @Email
    private String email;
    private String password;
    private String username;
    private String phoneNumber;
    private UserRole role;
}
