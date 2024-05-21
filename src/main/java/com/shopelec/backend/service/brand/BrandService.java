package com.shopelec.backend.service.brand;

import com.shopelec.backend.model.Brand;

public interface BrandService {
    Brand save(Brand brand);
    Brand update(Brand brand);
    void delete(Long id);
}
