package com.shopelec.backend.mapper;

import com.shopelec.backend.dto.request.ProductRequest;
import com.shopelec.backend.dto.request.SignupRequest;
import com.shopelec.backend.dto.request.UpdateUserRequest;
import com.shopelec.backend.dto.response.ProductResponse;
import com.shopelec.backend.dto.response.UserResponse;
import com.shopelec.backend.model.Product;
import com.shopelec.backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toProductResponse(Product product);
}
