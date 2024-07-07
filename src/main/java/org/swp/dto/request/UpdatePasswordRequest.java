package org.swp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class UpdatePasswordRequest {
    @NotNull
    private String oldPassword;
    @NotNull
    private String newPassword;
    @NotNull
    private String confirmPassword;
}
