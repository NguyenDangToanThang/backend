package com.shopelec.backend.controller.controller;

import com.shopelec.backend.dto.response.CouponsResponse;
import com.shopelec.backend.dto.response.UserResponse;
import com.shopelec.backend.service.coupons.CouponsService;
import com.shopelec.backend.service.fcm.FcmService;
import com.shopelec.backend.service.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/v1/admin/coupons")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CouponsController {

    CouponsService couponsService;
    FcmService fcmService;
    UserService userService;

    @GetMapping
    public String toCoupons(Model model) throws ExecutionException, InterruptedException {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse admin = userService.findByEmail(name);
        model.addAttribute("couponsList", couponsService.getAllCoupons());
        return "coupons";
    }

    @RequestMapping(path = "/update")
    public String updateCoupons(@ModelAttribute CouponsResponse response) {
        couponsService.updateCoupons(response);
        return "redirect:/v1/admin/coupons";
    }

    @RequestMapping(path = "/create")
    public String createCoupons(@ModelAttribute CouponsResponse response) throws ExecutionException, InterruptedException {
        couponsService.createCoupons(response);
        fcmService.sendNotificationAllUser("Thông báo","Mã giảm giá mới vừa được tung ra săn hàng ngay thôi");
        return "redirect:/v1/admin/coupons";
    }

}
