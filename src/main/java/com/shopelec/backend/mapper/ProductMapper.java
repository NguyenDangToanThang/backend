package com.shopelec.backend.mapper;

import com.shopelec.backend.dto.request.SignupRequest;
import com.shopelec.backend.dto.response.ProductResponse;
import com.shopelec.backend.model.Product;
import com.shopelec.backend.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toProductResponse(Product product);
    Product toProduct(ProductResponse request);
}
