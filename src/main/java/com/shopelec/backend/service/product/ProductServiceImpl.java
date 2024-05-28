package com.shopelec.backend.service.product;

import com.shopelec.backend.dto.request.ProductRequest;
import com.shopelec.backend.dto.response.ProductResponse;
import com.shopelec.backend.mapper.ProductMapper;
import com.shopelec.backend.model.Product;
import com.shopelec.backend.model.ProductSpecification;
import com.shopelec.backend.repository.ProductRepository;
import com.shopelec.backend.service.firebase.FirebaseStorageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProductServiceImpl implements ProductService{

    ProductRepository productRepository;
    ProductSpecificationService productSpecificationService;
    FirebaseStorageService firebaseStorageService;
    ProductMapper productMapper;

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public ProductResponse save(ProductRequest request, List<ProductSpecification> listData) throws IOException {

        Product product = productMapper.toProduct(request);
        product.setImage_url(firebaseStorageService.uploadFile(request.getImage_url()));
        Product response = productRepository.save(product);
        productSpecificationService.add(listData, response.getId());
        return productMapper.toProductResponse(response);
    }

}
