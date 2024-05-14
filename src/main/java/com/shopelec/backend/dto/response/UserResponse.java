package com.shopelec.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private String date_created;
    private String date_updated;
    private String phoneNumber;
    private String gender;
    private String dob;
}
