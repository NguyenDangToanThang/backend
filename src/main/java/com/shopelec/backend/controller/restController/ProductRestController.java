package com.shopelec.backend.controller.restController;


import com.shopelec.backend.dto.response.ProductResponse;
import com.shopelec.backend.dto.response.SpecificationResponse;
import com.shopelec.backend.mapper.ProductMapper;
import com.shopelec.backend.model.Product;
import com.shopelec.backend.model.ProductSpecification;
import com.shopelec.backend.service.product.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(allowedHeaders ="*",methods = {RequestMethod.POST , RequestMethod.GET})
@RequestMapping("/v1/api/product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductRestController {

    private static final Logger log = LoggerFactory.getLogger(ProductRestController.class);
    ProductService productService;
    ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProduct() {
        return ResponseEntity.ok(productService.getAllProduct());
    }

}
