package com.shopelec.backend.service.product;

import com.shopelec.backend.dto.request.FavoriteRequest;
import com.shopelec.backend.dto.request.ProductRequest;
import com.shopelec.backend.dto.response.*;
import com.shopelec.backend.mapper.ProductMapper;
import com.shopelec.backend.mapper.UserMapper;
import com.shopelec.backend.model.Favorite;
import com.shopelec.backend.model.Product;
import com.shopelec.backend.model.ProductSpecification;
import com.shopelec.backend.model.User;
import com.shopelec.backend.repository.*;
import com.shopelec.backend.service.firebase.FirebaseStorageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProductServiceImpl implements ProductService{

    static Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    BrandRepository brandRepository;
    FavoriteRepository favoriteRepository;
    UserRepository userRepository;

    ProductSpecificationService productSpecificationService;
    FirebaseStorageService firebaseStorageService;
    ProductMapper productMapper;

    @Override
    public Page<ProductResponse> getAllProduct(Pageable pageable,String user_id) {
        Page<Product> productsPage = productRepository.findAll(pageable);
        return productsPage.map(product -> convertToProductResponseUser(product,user_id));
    }

    @Override
    public Page<ProductResponse> findProductByCategoryId(Long category_id, Pageable pageable, String user_id) {
        Page<Product> productsPage = productRepository.findByCategoryId(category_id,pageable);
        return productsPage.map(product -> convertToProductResponseUser(product,user_id));
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

    @Override
    public boolean existById(Long id) {
        return productRepository.existsById(id);
    }

    @Override
    public List<ProductResponse> getAllProductAdmin() {
        return productRepository.findAll().stream().map(this::convertToProductResponse).toList();
    }

    @Override
    public List<FavoriteResponse> findAllFavoriteByUserId(String user_id) {
        List<FavoriteResponse> responses = new ArrayList<>();
        List<Favorite> favorites = favoriteRepository.findAllByUserId(user_id);
        for(Favorite favorite : favorites) {
            responses.add(FavoriteResponse.builder()
                            .productResponse(convertToProductResponseUser(favorite.getProduct(),user_id))
                            .id(favorite.getId())
                    .build());
        }
        return responses;
    }

    @Override
    public FavoriteResponse saveFavorite(FavoriteRequest request) {

        if(favoriteRepository.existsByUserIdAndProductId(request.getUser_id(), request.getProduct_id())) {
            return null;
        }
        else {
            Favorite favorite = favoriteRepository.save(
                    Favorite.builder()
                            .user(userRepository.findById(request.getUser_id()).orElseThrow(
                                    () -> new RuntimeException("User not found")
                            ))
                            .product(productRepository.findById(request.getProduct_id()).orElseThrow(
                                    () -> new RuntimeException("Product not found")
                            ))
                            .build()
            );
            return FavoriteResponse.builder()
                    .id(favorite.getId())
                    .productResponse(convertToProductResponse(favorite.getProduct()))
                    .build();
        }
    }

    @Override
    public void deleteFavorite(String user_id, Long product_id) {
        favoriteRepository.deleteByUserIdAndProductId(user_id,product_id);
    }

    private String convertImageUrl(String image_url) {
        return String.format("https://storage.googleapis.com/%s/%s", "shopelec-d93e6.appspot.com", image_url);
    }


    private ProductResponse convertToProductResponseUser(Product product, String user_id) {
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

        boolean favorite = favoriteRepository.existsByUserIdAndProductId(user_id, productResponse.getId());
        productResponse.setFavorite(favorite);

        List<SpecificationResponse> specificationResponses = new ArrayList<>();

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

        return productResponse;
    }

    private ProductResponse convertToProductResponse(Product product) {
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

        List<SpecificationResponse> specificationResponses = new ArrayList<>();

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

        return productResponse;
    }
}
