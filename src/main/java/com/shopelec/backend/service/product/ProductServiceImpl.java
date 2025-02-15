package com.shopelec.backend.service.product;

import com.shopelec.backend.dto.request.FavoriteRequest;
import com.shopelec.backend.dto.request.ProductRequest;
import com.shopelec.backend.dto.response.*;
import com.shopelec.backend.mapper.ProductMapper;
import com.shopelec.backend.model.Favorite;
import com.shopelec.backend.model.Product;
import com.shopelec.backend.model.ProductSpecification;
import com.shopelec.backend.model.Review;
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
    public Page<ProductResponse> getAllProduct(Pageable pageable,String user_id, String query) {

        if (query != null && !query.isEmpty()) {
            org.springframework.data.jpa.domain.Specification<Product> spec =
                    org.springframework.data.jpa.domain.Specification.where(
                            Specification.hasNameContaining(query))
                            .and(Specification.isNotDeleted());
            Page<Product> productsPage = productRepository.findAll(spec, pageable);
            return productsPage.map(product -> convertToProductResponseUser(product, user_id));
        } else {
            org.springframework.data.jpa.domain.Specification<Product> spec = org.springframework.data.jpa.domain.Specification.where(
                            Specification.isNotDeleted());
            Page<Product> productsPage = productRepository.findAll(spec, pageable);
            return productsPage.map(product -> convertToProductResponseUser(product, user_id));
        }
    }

    @Override
    public Page<ProductResponse> findProductByCategoryId(Long categoryId, Pageable pageable, String user_id, String query) {
        if (query != null && !query.isEmpty()) {
            org.springframework.data.jpa.domain.Specification<Product> spec =
                    org.springframework.data.jpa.domain.Specification.where(
                            Specification.hasCategoryId(categoryId))
                            .and(Specification.hasNameContaining(query))
                            .and(Specification.isNotDeleted());
            Page<Product> productsPage = productRepository.findAll(spec, pageable);
            return productsPage.map(product -> convertToProductResponseUser(product, user_id));
        } else {
            org.springframework.data.jpa.domain.Specification<Product> spec =
                    org.springframework.data.jpa.domain.Specification.where(
                            Specification.hasCategoryId(categoryId))
                            .and(Specification.isNotDeleted());
            Page<Product> productsPage = productRepository.findAll(spec, pageable);
            return productsPage.map(product -> convertToProductResponseUser(product, user_id));
        }
    }

    @Override
    public Page<ProductResponse> findProductByBrandId(Long brandId, Pageable pageable, String user_id, String query) {
        if (query != null && !query.isEmpty()) {
            org.springframework.data.jpa.domain.Specification<Product> spec =
                    org.springframework.data.jpa.domain.Specification.where(
                            Specification.hasBrandId(brandId))
                            .and(Specification.hasNameContaining(query))
                            .and(Specification.isNotDeleted());
            Page<Product> productsPage = productRepository.findAll(spec, pageable);
            return productsPage.map(product -> convertToProductResponseUser(product, user_id));
        } else {
            org.springframework.data.jpa.domain.Specification<Product> spec =
                    org.springframework.data.jpa.domain.Specification.where(
                            Specification.hasBrandId(brandId))
                            .and(Specification.isNotDeleted());
            Page<Product> productsPage = productRepository.findAll(spec, pageable);
            return productsPage.map(product -> convertToProductResponseUser(product, user_id));
        }
    }

    @Override
    public Page<ProductResponse> findProductByBrandIdAndCategoryId(Long brandId, Long categoryId, Pageable pageable, String user_id, String query) {
        if (query != null && !query.isEmpty()) {
            org.springframework.data.jpa.domain.Specification<Product> spec =
                    org.springframework.data.jpa.domain.Specification.where(
                            Specification.hasBrandId(brandId))
                            .and(Specification.hasCategoryId(categoryId))
                            .and(Specification.hasNameContaining(query))
                            .and(Specification.isNotDeleted());
            Page<Product> productsPage = productRepository.findAll(spec, pageable);
            return productsPage.map(product -> convertToProductResponseUser(product, user_id));
        } else {
            org.springframework.data.jpa.domain.Specification<Product> spec =
                    org.springframework.data.jpa.domain.Specification.where(
                            Specification.hasBrandId(brandId))
                            .and(Specification.hasCategoryId(categoryId))
                            .and(Specification.isNotDeleted());
            Page<Product> productsPage = productRepository.findAll(spec, pageable);
            return productsPage.map(product -> convertToProductResponseUser(product, user_id));
        }
    }

    @Override
    public ProductResponse save(ProductRequest request, List<ProductSpecification> listData) throws IOException {

        Product product = Product
                .builder()
                .price(request.getPrice())
                .description(request.getDescription())
                .name(request.getName())
                .discount(request.getDiscount())
                .status("Có sẵn")
                .quantity(request.getQuantity())
                .deleted(false)
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
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product not found")
        );
        product.setDeleted(true);
        productRepository.save(product);
    }

    @Override
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product not found")
        );
        return convertToProductResponse(product);
    }

    @Override
    public ProductResponse update(ProductRequest request, List<ProductSpecification> listData) throws IOException {
        Product product = productRepository.findById(request.getId()).orElseThrow(
                () -> new RuntimeException("Product not found")
        );
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setName(request.getName());
        product.setDiscount(request.getDiscount());
        product.setQuantity(request.getQuantity());
        if(!request.getImage_url().isEmpty()) {
            product.setImage_url(firebaseStorageService.uploadFile(request.getImage_url()));
        }
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
    public Page<ProductResponse> getTopProduct(Pageable pageable, String user_id) {
        org.springframework.data.jpa.domain.Specification<Product> spec = org.springframework.data.jpa.domain.Specification.where(
                Specification.isNotDeleted())
                .and(Specification.sortByReviewCountAndRating());
        Page<Product> productsPage = productRepository.findAll(spec, pageable);
        return productsPage.map(product -> convertToProductResponseUser(product, user_id));
    }

    @Override
    public Page<ProductResponse> getAllProductAdmin(Pageable pageable) {
        org.springframework.data.jpa.domain.Specification<Product> spec =
                org.springframework.data.jpa.domain.Specification.where(
                        Specification.isNotDeleted());
        return productRepository.findAll(spec,pageable)
                .map(this::convertToProductResponse);
    }

    @Override
    public Page<ProductResponse> getAllProductByStatus(Pageable pageable, String status) {
        org.springframework.data.jpa.domain.Specification<Product> spec =
                org.springframework.data.jpa.domain.Specification.where(
                        Specification.hasStatus(status)).
                        and(Specification.isNotDeleted());
        return productRepository.findAll(spec, pageable)
                .map(this::convertToProductResponse);
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

        List<ReviewResponse> reviewResponses = new ArrayList<>();
        List<Review> reviews = product.getReviews();

        for(Review review : reviews) {
            reviewResponses.add(ReviewResponse.builder()
                            .id(review.getId())
                            .like_comment(review.getLike_comment())
                            .rate(review.getRate())
                            .comment(review.getComment())
                            .date_created(review.getDate_created())
                            .email(review.getUser().getEmail())
                            .name(review.getUser().getName())
                            .imageUrl(convertImageUrl(review.getUser().getImageUrl()))
                    .build());
        }

        productResponse.setReviews(reviewResponses);
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
