package com.shopelec.backend.service.brand;

import com.shopelec.backend.dto.request.BrandRequest;
import com.shopelec.backend.dto.response.BrandResponse;
import com.shopelec.backend.dto.response.CategoryResponse;
import com.shopelec.backend.model.Brand;
import com.shopelec.backend.model.Category;
import com.shopelec.backend.repository.BrandRepository;
import com.shopelec.backend.service.firebase.FirebaseStorageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandServiceImpl implements BrandService{

    BrandRepository brandRepository;

    @Override
    public Brand save(BrandRequest request) throws IOException {
        if(request == null) {
            throw new NullPointerException("Brand cannot be null");
        } else if(brandRepository.existsByName(request.getName())) {
            throw new RuntimeException("Brand already exists");
        }
        Brand brand = Brand
                .builder()
                .name(request.getName())
                .build();
        return brandRepository.save(brand);
    }

    @Override
    public Brand update(BrandRequest request) throws IOException {
        if(request == null) {
            throw new NullPointerException("Brand cannot be null");
        } else if(!brandRepository.existsByName(request.getName())) {
            throw new RuntimeException("Brand not found");
        }
        Brand brand = Brand
                .builder()
                .id(request.getId())
                .name(request.getName())
                .build();
        return brandRepository.save(brand);
    }

    @Override
    public boolean delete(Long id) {
        if(brandRepository.existsById(id)) {
            brandRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean existByName(String name) {
        return brandRepository.existsByName(name);
    }

    @Override
    public List<BrandResponse> getAllBrand() {
        List<Brand> brands = brandRepository.findAll();
        List<BrandResponse> responses = new ArrayList<>();
        for(Brand brand : brands) {
            responses.add(BrandResponse
                    .builder()
                    .id(brand.getId())
                    .name(brand.getName())
                    .build()
            );
        }
        return responses;
    }
}
