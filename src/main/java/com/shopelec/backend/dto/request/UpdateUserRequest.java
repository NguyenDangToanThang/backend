package com.shopelec.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    private String email;
    private String name;
    private String phoneNumber;
    private String gender;
    private String dob;
}
