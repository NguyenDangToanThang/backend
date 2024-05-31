package com.shopelec.backend.controller.controller;

import com.shopelec.backend.dto.response.UserResponse;
import com.shopelec.backend.model.Brand;
import com.shopelec.backend.model.Product;
import com.shopelec.backend.service.brand.BrandService;
import com.shopelec.backend.service.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/v1/admin/brand")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class BrandController {

    UserService userService;
    BrandService brandService;

    @GetMapping
    public String toBrand(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse admin = userService.findByEmail(name);
        List<Brand> brands = brandService.getAllBrand();
        model.addAttribute("admin", admin);
        model.addAttribute("brands",brands);
        for (Brand brand : brands) {
            log.info("Brands: {}", brand.getName());
            for (Product product : brand.getProducts()) {
                log.info("Products: {}",product.getName());
            }
        }
        log.info("Brands: {}" , brands);
        return "brand";
    }

    @PostMapping("/add")
    public String addBrand(Brand brand, RedirectAttributes redirectAttributes) {
        if(brandService.existByName(brand.getName())) {
            redirectAttributes.addFlashAttribute("error", "Brand already exists");
        } else {
            Brand result = brandService.save(brand);
            redirectAttributes.addFlashAttribute("message","Create brand successfully");
        }
        log.info(redirectAttributes.getFlashAttributes().toString());
        return "redirect:/v1/admin/brand";
    }

    @PostMapping("/update")
    public String updateBrand(Brand brand, RedirectAttributes redirectAttributes) {
        if(brandService.existByName(brand.getName())) {
            redirectAttributes.addFlashAttribute("error", "Brand already exists");
        } else {
            brandService.update(brand);
            redirectAttributes.addFlashAttribute("message", "Update brand successfully");
        }
        log.info(redirectAttributes.getFlashAttributes().toString());
        return "redirect:/v1/admin/brand";
    }

    @GetMapping("/delete/{id}")
    public String deleteBrand(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean check = brandService.delete(id);
        if(check) {
            redirectAttributes.addFlashAttribute("message", "Delete brand successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Delete brand failed");
        }
        log.info(redirectAttributes.getFlashAttributes().toString());
        return "redirect:/v1/admin/brand";
    }
}
