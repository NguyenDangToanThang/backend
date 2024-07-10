package com.shopelec.backend.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    LocalDateTime orderDate;
    LocalDateTime modifiedDate;
    double totalPrice;
    String status;

    public String getOrderDateFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return this.orderDate.format(formatter);
    }
    public String getModifiedDateFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return this.modifiedDate.format(formatter);
    }

    Long coupon_id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;


    @ManyToOne
    @JoinColumn(name = "address_id")
    Address address;

}
