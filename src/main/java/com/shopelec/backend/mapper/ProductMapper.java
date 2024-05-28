package com.shopelec.backend.mapper;

import com.shopelec.backend.dto.request.ProductRequest;
import com.shopelec.backend.dto.request.SignupRequest;
import com.shopelec.backend.dto.request.UpdateUserRequest;
import com.shopelec.backend.dto.response.ProductResponse;
import com.shopelec.backend.dto.response.UserResponse;
import com.shopelec.backend.model.Product;
import com.shopelec.backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductRequest request);
    void updateProduct(@MappingTarget Product product, ProductRequest request);
    ProductResponse toProductResponse(Product product);
}
