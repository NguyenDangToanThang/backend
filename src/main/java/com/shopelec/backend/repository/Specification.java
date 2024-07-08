package com.shopelec.backend.repository;

import com.shopelec.backend.model.Product;
import com.shopelec.backend.model.Review;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

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

    public static org.springframework.data.jpa.domain.Specification<Product> sortByReviewCountAndRating() {
        return (root, query, criteriaBuilder) -> {
            Join<Product, Review> reviews = root.join("reviews", JoinType.INNER);

            query.groupBy(root.get("id"));
            query.orderBy(
                    criteriaBuilder.desc(criteriaBuilder.count(reviews.get("id"))),
                    criteriaBuilder.desc(criteriaBuilder.avg(reviews.get("rate")))
            );

            return criteriaBuilder.conjunction();
        };
    }

}
