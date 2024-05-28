package com.shopelec.backend.controller.controller;

import com.shopelec.backend.dto.response.UserResponse;
import com.shopelec.backend.service.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1/admin/order")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    UserService userService;

    @GetMapping
    public String toOrder(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse admin = userService.findByEmail(name);
        model.addAttribute("admin", admin);
        return "order";
    }
}
