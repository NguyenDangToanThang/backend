package com.shopelec.backend.service.product;

import com.shopelec.backend.model.ProductSpecification;

import java.util.List;

public interface ProductSpecificationService {
    void add(List<ProductSpecification> request, Long product_id);
}
