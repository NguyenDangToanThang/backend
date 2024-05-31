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
    public List<ProductSpecification> add(List<ProductSpecification> request, Long product_id) {

        List<ProductSpecification> list = new ArrayList<ProductSpecification>();

        for (ProductSpecification productSpecification : request) {
            if (!repository.existsByNameAndProductId(productSpecification.getName(), product_id)) {
                productSpecification.setProduct(productRepository.findById(product_id).orElseThrow(
                        () -> new RuntimeException("Product not found")
                ));
                list.add(productSpecification);
            }
        }
        if (list.isEmpty()) {
            throw new RuntimeException("Add product specification failed");
        }
        return repository.saveAll(list);
    }

}
