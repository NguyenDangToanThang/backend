package com.shopelec.backend.controller.controller;

import com.google.firebase.auth.FirebaseAuthException;
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
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    FirebaseService firebaseService;
    UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model, @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "5") int pageSize) throws FirebaseAuthException {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse admin = userService.findByEmail(name);
        List<UserResponseFirebase> response = firebaseService.listAllUsers(page,pageSize);
        int totalUsersCount = firebaseService.getTotalUsersCount();
        int totalPages = totalUsersCount / pageSize + (totalUsersCount % pageSize == 0 ? 0 : 1);
        model.addAttribute("users", response);
        model.addAttribute("admin", admin);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "admin";
    }
}
