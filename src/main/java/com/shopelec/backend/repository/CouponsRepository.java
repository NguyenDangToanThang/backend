package com.shopelec.backend.repository;

import com.shopelec.backend.model.Coupons;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponsRepository extends JpaRepository<Coupons, Long> {
    Coupons findByCode(String code);
}
