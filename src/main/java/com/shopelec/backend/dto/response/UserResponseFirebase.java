package com.shopelec.backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseFirebase {
    private String email;
    private String created;
    private String signedIn;
    private String uid;
}
