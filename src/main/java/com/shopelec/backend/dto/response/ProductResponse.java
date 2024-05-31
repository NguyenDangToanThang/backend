package com.shopelec.backend.dto.response;

import com.shopelec.backend.model.Brand;
import com.shopelec.backend.model.Category;
import com.shopelec.backend.model.ProductSpecification;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductResponse {
    Long id;
    String name;
    @Column(length = 1000)
    String description;
    double price;
    int discount;
    int quantity;
    String image_url;
    String status;
    BrandResponse brand;
    CategoryResponse category;
    List<SpecificationResponse> specifications;
}
