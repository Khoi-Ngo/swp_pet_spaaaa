package org.swp.dto.response;

import lombok.Data;

@Data
public class DetailAccountDto {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String phone;
    private boolean status;
}
