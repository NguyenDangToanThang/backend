package com.shopelec.backend.controller.restController;

import com.shopelec.backend.dto.response.BrandResponse;
import com.shopelec.backend.dto.response.CategoryResponse;
import com.shopelec.backend.service.brand.BrandService;
import com.shopelec.backend.service.category.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/brand")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class BrandRestController {

    BrandService brandService;

    @GetMapping
    public ResponseEntity<List<BrandResponse>> getAllBrand () {
        return ResponseEntity.ok(brandService.getAllBrand());
    }
}
