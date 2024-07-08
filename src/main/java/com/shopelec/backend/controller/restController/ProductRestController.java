package com.shopelec.backend.controller.restController;


import com.shopelec.backend.dto.response.ProductResponse;
import com.shopelec.backend.service.product.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(allowedHeaders ="*",methods = {RequestMethod.POST , RequestMethod.GET})
@RequestMapping(value = "/v1/api/product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductRestController {

    ProductService productService;

    @GetMapping("/top")
    public ResponseEntity<?> getTopProduct(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "4") int size,
                                           @RequestParam(required = false) String user_id) {
        Pageable paging = PageRequest.of(page,size);
        Page<ProductResponse> pageProducts = productService.getTopProduct(paging,user_id);
        Map<String, Object> response = new HashMap<>();
        response.put("products", pageProducts.getContent());
        response.put("currentPage", pageProducts.getNumber());
        response.put("totalItems", pageProducts.getTotalElements());
        response.put("totalPages", pageProducts.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProduct(@RequestParam(defaultValue = "0") Long category_id,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "4") int size,
                                                             @RequestParam(defaultValue = "id,desc") String[] sort,
                                                             @RequestParam(required = false) String user_id,
                                                             @RequestParam(defaultValue = "0") Long brand_id,
                                                             @RequestParam(defaultValue = "") String query) {
        try {
            String sortField = sort[0];
            Sort.Direction sortDirection = Sort.Direction.fromString(sort[1]);
            Sort sorting = Sort.by(sortDirection,sortField);
            Pageable paging = PageRequest.of(page,size,sorting);
            Page<ProductResponse> pageProducts;
            if(category_id == 0 && brand_id == 0) {
                pageProducts = productService.getAllProduct(paging,user_id,query);
            } else if(category_id != 0 && brand_id == 0){
                pageProducts = productService.findProductByCategoryId(category_id,paging,user_id,query);
            } else if(category_id == 0 && brand_id != 0) {
                pageProducts = productService.findProductByBrandId(brand_id,paging,user_id,query);
            } else {
                pageProducts = productService.findProductByBrandIdAndCategoryId(brand_id,category_id,paging,user_id,query);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("products", pageProducts.getContent());
            response.put("currentPage", pageProducts.getNumber());
            response.put("totalItems", pageProducts.getTotalElements());
            response.put("totalPages", pageProducts.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
