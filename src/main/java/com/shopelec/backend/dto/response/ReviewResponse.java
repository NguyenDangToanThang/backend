package com.shopelec.backend.dto.response;

import com.shopelec.backend.model.Product;
import com.shopelec.backend.model.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ReviewResponse {
    Long id;
    String comment;
    double rate;
    int like_comment;
    String date_created;
    String email;
    String name;
    String imageUrl;
}
