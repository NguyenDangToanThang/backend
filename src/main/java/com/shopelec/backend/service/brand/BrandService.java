package com.shopelec.backend.service.brand;

import com.shopelec.backend.model.Brand;

import java.util.List;

public interface BrandService {
    Brand save(Brand brand);
    Brand update(Brand brand);
    boolean delete(Long id);
    boolean existByName(String name);
    List<Brand> getAllBrand();
}
