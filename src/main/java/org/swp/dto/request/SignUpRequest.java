package org.swp.dto.request;

import lombok.Data;
import org.swp.enums.UserRole;

@Data
public class SignUpRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String username;
    private UserRole role;

}
