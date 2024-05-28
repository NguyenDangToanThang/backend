package com.shopelec.backend.controller.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopelec.backend.dto.request.ProductRequest;
import com.shopelec.backend.dto.response.ProductResponse;
import com.shopelec.backend.dto.response.UserResponse;
import com.shopelec.backend.model.ProductSpecification;
import com.shopelec.backend.service.brand.BrandService;
import com.shopelec.backend.service.category.CategoryService;
import com.shopelec.backend.service.product.ProductService;
import com.shopelec.backend.service.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/v1/admin/product")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    UserService userService;
    ProductService productService;
    BrandService brandService;
    CategoryService categoryService;

    @GetMapping
    public String toProduct(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse admin = userService.findByEmail(name);
        model.addAttribute("admin", admin);
        model.addAttribute("products", productService.getAllProduct());
        return "product/product";
    }

    @GetMapping("/add")
    public String toAddProduct(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse admin = userService.findByEmail(name);
        model.addAttribute("admin", admin);
        model.addAttribute("brands", brandService.getAllBrand());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "product/add";
    }
    @PostMapping("/add")
    public String addProduct(@RequestBody ProductRequest request,
                             @RequestParam("featuresInput") String featuresJSON,
                             Model model) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        List<ProductSpecification> listData = mapper.readValue(featuresJSON, new TypeReference<List<ProductSpecification>>() {});

        ProductResponse response = productService.save(request,listData);
        log.info("Response: {}",response);
        if(response != null) {
            model.addAttribute("message", "Add product successfully");
        } else {
            model.addAttribute("error", "Add product failed");
        }

        return "product/add";
    }
}
