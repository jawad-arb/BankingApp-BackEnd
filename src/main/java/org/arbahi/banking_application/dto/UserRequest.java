package org.arbahi.banking_application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String firstName;
    private String lastName;
    private String otherName;
    private String gender;
    private String stateOfOrigin;
    private String email;
    private String password;
    private String phoneNumber;
    private String alternativePhoneNumber;
}
