package org.swp.dto.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class UpdatePasswordRequest {
    @NonNull
    private String oldPassword;
    @NonNull
    private String newPassword;
    @NonNull
    private String confirmPassword;
}
