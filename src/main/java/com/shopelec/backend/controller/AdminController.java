package com.shopelec.backend.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.shopelec.backend.dto.response.UserResponse;
import com.shopelec.backend.dto.response.UserResponseFirebase;
import com.shopelec.backend.service.firebase.FirebaseService;
import com.shopelec.backend.service.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/v1/admin")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class AdminController {

    FirebaseService firebaseService;
    UserService userService;

    @GetMapping("/category")
    public String toCategory(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse admin = userService.findByEmail(name);
        model.addAttribute("admin", admin);
        return "category";
    }

    @GetMapping("/order")
    public String toOrder(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse admin = userService.findByEmail(name);
        model.addAttribute("admin", admin);
        return "order";
    }

    @GetMapping("/brand")
    public String toBrand(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse admin = userService.findByEmail(name);
        model.addAttribute("admin", admin);
        return "brand";
    }

    @GetMapping("/product")
    public String toProduct(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse admin = userService.findByEmail(name);
        model.addAttribute("admin", admin);
        return "product";
    }

    @GetMapping("/statistic")
    public String toStatistic(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse admin = userService.findByEmail(name);
        model.addAttribute("admin", admin);
        return "statistic";
    }


    @GetMapping("/user/delete/{email}")
    public String deleteUser(@PathVariable String email, Model model) throws FirebaseAuthException {
        firebaseService.deleteUserFirebase(email);
        model.addAttribute("message", "Delete successful");
        return "redirect:/home";
    }

}
