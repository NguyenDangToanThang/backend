package com.shopelec.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String email;
    private String name;
    private String date_created;
    private String date_updated;
    private String phoneNumber;
    private String gender;
    private String dob;
}
