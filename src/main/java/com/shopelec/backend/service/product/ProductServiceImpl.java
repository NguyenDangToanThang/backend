package com.shopelec.backend.service.product;

import com.shopelec.backend.dto.request.ProductRequest;
import com.shopelec.backend.dto.response.BrandResponse;
import com.shopelec.backend.dto.response.CategoryResponse;
import com.shopelec.backend.dto.response.ProductResponse;
import com.shopelec.backend.dto.response.SpecificationResponse;
import com.shopelec.backend.mapper.ProductMapper;
import com.shopelec.backend.model.Product;
import com.shopelec.backend.model.ProductSpecification;
import com.shopelec.backend.repository.BrandRepository;
import com.shopelec.backend.repository.CategoryRepository;
import com.shopelec.backend.repository.ProductRepository;
import com.shopelec.backend.service.firebase.FirebaseStorageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProductServiceImpl implements ProductService{

    static Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    BrandRepository brandRepository;

    ProductSpecificationService productSpecificationService;
    FirebaseStorageService firebaseStorageService;
    ProductMapper productMapper;

    @Override
    public List<ProductResponse> getAllProduct() {
        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<ProductResponse> responses = new ArrayList<ProductResponse>();
        for(Product product : products) {
            ProductResponse productResponse = ProductResponse
                    .builder()
                    .id(product.getId())
                    .brand(BrandResponse
                            .builder()
                            .id(product.getBrand().getId())
                            .name(product.getBrand().getName())
                            .build())
                    .category(CategoryResponse
                            .builder()
                            .id(product.getCategory().getId())
                            .name(product.getCategory().getName())
                            .build())
                    .image_url(convertImageUrl(product.getImage_url()))
                    .description(product.getDescription())
                    .discount(product.getDiscount())
                    .name(product.getName())
                    .quantity(product.getQuantity())
                    .price(product.getPrice())
                    .status(product.getStatus())
                    .build();

            List<SpecificationResponse> specificationResponses = new ArrayList<SpecificationResponse>();

            List<ProductSpecification> specifications = product.getProductSpecifications();
            for (ProductSpecification specification : specifications) {
                SpecificationResponse specificationResponse = SpecificationResponse
                        .builder()
                        .id(specification.getId())
                        .name(specification.getName())
                        .description(specification.getDescription())
                        .build();
                specificationResponses.add(specificationResponse);
            }

            productResponse.setSpecifications(specificationResponses);

            responses.add(productResponse);
        }

        return responses;
    }

    @Override
    public ProductResponse save(ProductRequest request, List<ProductSpecification> listData) throws IOException {

        Product product = Product
                .builder()
                .price(request.getPrice())
                .description(request.getDescription())
                .name(request.getName())
                .discount(request.getDiscount())
                .status("Enable")
                .quantity(request.getQuantity())
                .build();
        product.setImage_url(firebaseStorageService.uploadFile(request.getImage_url()));
        product.setCategory(categoryRepository.findById(request.getCategory_id()).orElseThrow(
                () -> new RuntimeException("Category not found")
        ));
        product.setBrand(brandRepository.findById(request.getBrand_id()).orElseThrow(
                () -> new RuntimeException("Brand not found")
        ));

        Product response = productRepository.save(product);
        productSpecificationService.add(listData, response.getId());
        return productMapper.toProductResponse(response);
    }

    private String convertImageUrl(String image_url) {
        return String.format("https://storage.googleapis.com/%s/%s", "shopelec-d93e6.appspot.com", image_url);
    }
}
