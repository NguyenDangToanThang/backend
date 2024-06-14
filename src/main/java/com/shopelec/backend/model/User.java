package com.shopelec.backend.model;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    String id;
    String email;
    String password;
    String name;
    String date_created;
    String date_updated;
    String phoneNumber;
    String gender;
    String dob;
    String role;
    String imageUrl;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Cart> carts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Favorite> favorites;
}
