package com.shopelec.backend.repository;

import com.shopelec.backend.model.Favorite;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Transactional
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findAllByUserId(String user_id);
    boolean existsByUserIdAndProductId(String user_id, Long product_id);
    void deleteByUserIdAndProductId(String user_id, Long product_id);
}
