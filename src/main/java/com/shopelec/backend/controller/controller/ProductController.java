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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String toProduct(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size,
                            @RequestParam(defaultValue = "") String status) {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse admin = userService.findByEmail(name);
        model.addAttribute("admin", admin);
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> productPage;

        if (!status.isEmpty()) {
            productPage = productService.getAllProductByStatus(pageable, status);
        } else {
            productPage = productService.getAllProductAdmin(pageable);
        }

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("status", status);
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

    @GetMapping("/update/{id}")
    public String toUpdateProduct(Model model, @PathVariable Long id) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse admin = userService.findByEmail(name);
        ProductResponse productResponse = productService.findById(id);
        model.addAttribute("admin", admin);
        model.addAttribute("brands", brandService.getAllBrand());
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("product", productResponse);
        model.addAttribute("specifications", productResponse.getSpecifications());
        model.addAttribute("price", (long) productResponse.getPrice());
        return "product/update";
    }

    @PostMapping("/update")
    public String updateProduct(ProductRequest request,
                             @RequestParam("featuresInput") String featuresJSON,
                             RedirectAttributes redirectAttributes) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        List<ProductSpecification> listData = mapper.readValue(featuresJSON, new TypeReference<List<ProductSpecification>>() {});

        ProductResponse response = productService.update(request,listData);
        if(response != null) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật sản phẩm thành công");
        } else {
            redirectAttributes.addFlashAttribute("error", "Cập nhật sản phẩm thất bại");
        }

        return "redirect:/v1/admin/product";
    }



    @PostMapping("/add")
    public String addProduct(ProductRequest request,
                             @RequestParam("featuresInput") String featuresJSON,
                             RedirectAttributes redirectAttributes) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        List<ProductSpecification> listData = mapper.readValue(featuresJSON, new TypeReference<List<ProductSpecification>>() {});

        ProductResponse response = productService.save(request,listData);
        if(response != null) {
            redirectAttributes.addFlashAttribute("message", "Thêm sản phẩm thành công");
        } else {
            redirectAttributes.addFlashAttribute("error", "Thêm sản phẩm thất bại");
        }
        return "redirect:/v1/admin/product";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(RedirectAttributes redirectAttributes, @PathVariable Long id) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("message", "Xóa sản phẩm thành công");
        return "redirect:/v1/admin/product";
    }
}
