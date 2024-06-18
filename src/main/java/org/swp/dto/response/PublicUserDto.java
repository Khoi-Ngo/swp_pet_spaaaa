package org.swp.dto.response;

import jakarta.persistence.Lob;
import lombok.Data;
import org.swp.enums.UserRole;

import java.time.LocalDate;

@Data
public class PublicUserDto {

    //account information
    private String username;
    private UserRole role;
    //fundamental personal information
    private String firstName;
    private String lastName;
    //other
    @Lob
    private String profileImageUrl;
    @Lob
    private String coverImageUrl;
    private LocalDate birthday;
}
