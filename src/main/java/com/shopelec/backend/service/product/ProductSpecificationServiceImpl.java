package com.shopelec.backend.service.product;

import com.shopelec.backend.model.ProductSpecification;
import com.shopelec.backend.repository.ProductRepository;
import com.shopelec.backend.repository.ProductSpecificationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProductSpecificationServiceImpl implements ProductSpecificationService{

    ProductSpecificationRepository repository;
    ProductRepository productRepository;

    @Override
    public void add(List<ProductSpecification> request, Long product_id) {

        List<ProductSpecification> productSpecifications = repository.findAllByProductId(product_id);
        for(ProductSpecification specification : productSpecifications) {
            for(ProductSpecification specification1 : request) {
                if(specification1.equals(specification)) {
                    productSpecifications.remove(specification1);
                }
            }
        }

        if(!productSpecifications.isEmpty()) {
            repository.deleteAll(productSpecifications);
        }

        for (ProductSpecification productSpecification : request) {
            if (!repository.existsByNameAndProductId(productSpecification.getName(), product_id)) {
                productSpecification.setProduct(productRepository.findById(product_id).orElseThrow(
                        () -> new RuntimeException("Product not found")
                ));
                repository.save(productSpecification);
            } else {
                ProductSpecification specification = repository.findByNameAndProductId(productSpecification.getName(), product_id);
                specification.setDescription(productSpecification.getDescription());
                repository.save(specification);
            }
        }
    }

}
