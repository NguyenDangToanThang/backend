package com.shopelec.backend.controller.controller;

import com.shopelec.backend.dto.request.CategoryRequest;
import com.shopelec.backend.dto.response.CategoryResponse;
import com.shopelec.backend.dto.response.UserResponse;
import com.shopelec.backend.model.Category;
import com.shopelec.backend.service.category.CategoryService;
import com.shopelec.backend.service.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/v1/admin/category")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    UserService userService;
    CategoryService categoryService;

    @GetMapping
    public String toCategory(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse admin = userService.findByEmail(name);
        List<CategoryResponse> categories = categoryService.getAllCategory();
        model.addAttribute("admin", admin);
        model.addAttribute("categories", categories);
        return "category";
    }

    @PostMapping("/add")
    public String addCategory(CategoryRequest category, RedirectAttributes redirectAttrs) throws IOException {
        if(categoryService.existByName(category.getName())) {
            redirectAttrs.addFlashAttribute("error", "Category already exists");
        } else {
            Category result = categoryService.save(category);
            log.info(category.getName());
            redirectAttrs.addFlashAttribute("message","Create category successfully");
        }
        log.info(redirectAttrs.getFlashAttributes().toString());
        return "redirect:/v1/admin/category";
    }

    @PostMapping("/update")
    public String updateCategory(CategoryRequest category, RedirectAttributes redirectAttributes) throws IOException {
        log.info(category.getId().toString());
        if(categoryService.existByName(category.getName())) {
            redirectAttributes.addFlashAttribute("error", "Category already exists");
        } else {
            categoryService.update(category);
            redirectAttributes.addFlashAttribute("message", "Update category successfully");
        }
        return "redirect:/v1/admin/category";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, Model model, RedirectAttributes redirectAttrs) {
        boolean check = categoryService.delete(id);
        if(check) {
            redirectAttrs.addFlashAttribute("message", "Delete category successfully");
        } else {
            redirectAttrs.addFlashAttribute("error", "Delete Failed");
        }
        log.info(redirectAttrs.getFlashAttributes().toString());
        return "redirect:/v1/admin/category";
    }
}
