package com.shopelec.backend.controller.controller;

import com.shopelec.backend.dto.request.BrandRequest;
import com.shopelec.backend.dto.response.BrandResponse;
import com.shopelec.backend.dto.response.UserResponse;
import com.shopelec.backend.model.Brand;
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

import java.io.IOException;
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
        List<Brand> brands = brandService.getAll();
        model.addAttribute("admin", admin);
        model.addAttribute("brands",brands);
        return "brand";
    }

    @PostMapping("/add")
    public String addBrand(BrandRequest request, RedirectAttributes redirectAttributes) throws IOException {
        if(brandService.existByName(request.getName())) {
            redirectAttributes.addFlashAttribute("error", "Thương hiệu đã tồn tại");
        } else {
            Brand result = brandService.save(request);
            redirectAttributes.addFlashAttribute("message","Tạo thương hiệu thành công");
        }
        return "redirect:/v1/admin/brand";
    }

    @PostMapping("/update")
    public String updateBrand(BrandRequest brand, RedirectAttributes redirectAttributes) throws IOException {
        if(brandService.existByName(brand.getName())) {
            redirectAttributes.addFlashAttribute("error", "Thương hiệu đã tồn tại");
        } else {
            brandService.update(brand);
            redirectAttributes.addFlashAttribute("message", "Cập nhật thương hiệu thành công");
        }
        return "redirect:/v1/admin/brand";
    }

    @GetMapping("/delete/{id}")
    public String deleteBrand(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean check = brandService.delete(id);
        if(check) {
            redirectAttributes.addFlashAttribute("message", "Xóa thương hiệu thành công");
        } else {
            redirectAttributes.addFlashAttribute("error", "Xóa thương hiệu không thành công");
        }
        return "redirect:/v1/admin/brand";
    }
}
