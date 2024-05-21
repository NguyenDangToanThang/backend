package com.shopelec.backend.service.brand;

import com.shopelec.backend.model.Brand;
import com.shopelec.backend.repository.BrandRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandServiceImpl implements BrandService{

    BrandRepository brandRepository;

    @Override
    public Brand save(Brand brand) {
        if(brand == null) {
            throw new NullPointerException("Brand cannot be null");
        } else if(brandRepository.existsByName(brand.getName())) {
            throw new RuntimeException("Brand already exists");
        }
        return brandRepository.save(brand);
    }

    @Override
    public Brand update(Brand brand) {
        if(brand == null) {
            throw new NullPointerException("Brand cannot be null");
        } else if(!brandRepository.existsById(brand.getId())) {
            throw new RuntimeException("Brand not found");
        }
        return brandRepository.save(brand);
    }

    @Override
    public void delete(Long id) {
        brandRepository.deleteById(id);
    }
}
