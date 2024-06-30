package com.shopelec.backend.repository;

import com.shopelec.backend.model.Product;

public class Specification {
    public static org.springframework.data.jpa.domain.Specification<Product> isNotDeleted() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("deleted"));
    }

    public static org.springframework.data.jpa.domain.Specification<Product> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

    public static org.springframework.data.jpa.domain.Specification<Product> hasNameContaining(String query1) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + query1.toLowerCase() + "%");
    }

    public static org.springframework.data.jpa.domain.Specification<Product> hasCategoryId(Long categoryId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    public static org.springframework.data.jpa.domain.Specification<Product> hasBrandId(Long brandId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("brand").get("id"), brandId);
    }
}
