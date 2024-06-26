package com.shopelec.backend.service.product;

import com.shopelec.backend.dto.request.FavoriteRequest;
import com.shopelec.backend.dto.request.ProductRequest;
import com.shopelec.backend.dto.response.FavoriteResponse;
import com.shopelec.backend.dto.response.ProductResponse;
import com.shopelec.backend.model.Product;
import com.shopelec.backend.model.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    Page<ProductResponse> getAllProduct(Pageable pageable, String user_id, String query);
    Page<ProductResponse> findProductByCategoryId(Long category_id, Pageable pageable, String user_id,String query);
    Page<ProductResponse> findProductByBrandId(Long brand_id, Pageable pageable, String user_id,String query);
    Page<ProductResponse> findProductByBrandIdAndCategoryId(Long category_id,Long brand_id, Pageable pageable, String user_id, String query);
    ProductResponse save(ProductRequest request, List<ProductSpecification> listData) throws IOException;
    boolean existById(Long id);
    List<ProductResponse> getAllProductAdmin();
    List<FavoriteResponse> findAllFavoriteByUserId(String user_id);
    FavoriteResponse saveFavorite(FavoriteRequest request);
    void deleteFavorite(String user_id,Long product_id);

}
