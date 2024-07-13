package org.swp.dto.request;

import lombok.Data;

@Data
public class PasswordChangeRequest {
    private String token;
    private String newPassword;
    private String confirmPassword;
}
