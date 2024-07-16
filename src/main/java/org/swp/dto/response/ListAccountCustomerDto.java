package org.swp.dto.response;

import lombok.Data;
import org.swp.enums.UserRole;
import java.time.LocalDate;

@Data
public class ListAccountCustomerDto {
        private Integer id;
        private String username;
        private String password;
        private UserRole role;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String profileImageUrl;
        private String coverImageUrl;
        private LocalDate birthday;
        private boolean isDeleted;
}
