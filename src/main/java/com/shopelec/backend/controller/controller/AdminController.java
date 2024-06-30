package com.shopelec.backend.controller.controller;

import com.shopelec.backend.dto.request.ChangePasswordRequest;
import com.shopelec.backend.dto.request.UpdateUserRequest;
import com.shopelec.backend.dto.response.UserResponse;
import com.shopelec.backend.service.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/v1/admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    UserService userService;

    @GetMapping("/user/{id}")
    public String detailUser(@PathVariable String id, Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse user = userService.findById(id);
        model.addAttribute("user", user);
        UserResponse admin = userService.findByEmail(name);
        model.addAttribute("admin", admin);
        return "user-details";
    }


    @PostMapping("/profile")
    public String profileHandle(UpdateUserRequest request, Model model) {
        UserResponse response = userService.update(request);
        if(response != null) {
            model.addAttribute("message", "Cập nhật tài khoản quản trị thành công");
        } else {
            model.addAttribute("error", "Cập nhật tài khoản quản trị thất bại");
        }
        return "redirect:/home";
    }

    @PostMapping("/change_password")
    public String changePasswordHandle(@RequestBody ChangePasswordRequest request, Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if(userService.changePasswordAdmin(name,request)){
            model.addAttribute("message", "Đổi mật khẩu thành công");
        }
        return "redirect:/home";
    }
}
