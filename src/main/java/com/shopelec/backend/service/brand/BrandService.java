package com.shopelec.backend.service.brand;

import com.shopelec.backend.dto.request.BrandRequest;
import com.shopelec.backend.dto.response.BrandResponse;
import com.shopelec.backend.model.Brand;

import java.io.IOException;
import java.util.List;

public interface BrandService {
    Brand save(BrandRequest request) throws IOException;
    Brand update(BrandRequest request) throws IOException;
    boolean delete(Long id);
    boolean existByName(String name);
    List<BrandResponse> getAllBrand();
    List<Brand> getAll();
}
