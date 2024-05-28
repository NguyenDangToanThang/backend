package com.shopelec.backend.service.product;

import com.shopelec.backend.dto.request.ProductRequest;
import com.shopelec.backend.dto.response.ProductResponse;
import com.shopelec.backend.model.Product;
import com.shopelec.backend.model.ProductSpecification;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    List<Product> getAllProduct();
    ProductResponse save(ProductRequest request, List<ProductSpecification> listData) throws IOException;
}
