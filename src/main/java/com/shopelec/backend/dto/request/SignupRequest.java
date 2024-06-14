package com.shopelec.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    private String id;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    // private String gender;
    // private String dob;
}
