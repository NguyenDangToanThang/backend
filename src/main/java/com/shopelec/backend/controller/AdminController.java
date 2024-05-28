package com.shopelec.backend.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.shopelec.backend.dto.request.ChangePasswordRequest;
import com.shopelec.backend.dto.request.UpdateUserRequest;
import com.shopelec.backend.dto.response.UserResponse;
import com.shopelec.backend.model.Brand;
import com.shopelec.backend.model.Category;
import com.shopelec.backend.service.brand.BrandService;
import com.shopelec.backend.service.category.CategoryService;
import com.shopelec.backend.service.firebase.FirebaseService;
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

import java.util.List;

@Controller
@RequestMapping("/v1/admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    FirebaseService firebaseService;
    UserService userService;

    @GetMapping("/user/delete/{email}")
    public String deleteUser(@PathVariable String email, Model model) throws FirebaseAuthException {
        firebaseService.deleteUserFirebase(email);
        model.addAttribute("message", "successMsg");
        return "redirect:/home";
    }

    @PostMapping("/profile")
    public String profileHandle(@RequestBody UpdateUserRequest request, Model model) {
        UserResponse response = userService.update(request);
        if(response != null) {
            model.addAttribute("message", "successMsg");
        } else {
            model.addAttribute("error", "errorMsg");
        }
        return "redirect:/home";
    }

    @PostMapping("/change_password")
    public String changePasswordHandle(@RequestBody ChangePasswordRequest request, Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if(userService.changePasswordAdmin(name,request)){
            model.addAttribute("message", "successMsg");
        }
        return "redirect:/home";
    }
}
