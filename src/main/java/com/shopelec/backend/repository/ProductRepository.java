package com.shopelec.backend.repository;

import com.shopelec.backend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryId(Long id,Pageable pageable);
    Page<Product> findByBrandId(Long id,Pageable pageable);
    Page<Product> findByBrandIdAndCategoryId(Long brand_id,Long category_id,Pageable pageable);
}
