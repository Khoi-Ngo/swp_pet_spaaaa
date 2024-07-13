package org.swp.dto.request;

import lombok.Data;

@Data
public class PasswordChangeRequest {
    private String newPassword;
    private String confirmPassword;
}
