package com.shopelec.backend.controller.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.shopelec.backend.dto.response.OrderDetailResponse;
import com.shopelec.backend.dto.response.UserResponse;
import com.shopelec.backend.model.Coupons;
import com.shopelec.backend.model.Order;
import com.shopelec.backend.service.coupons.CouponsService;
import com.shopelec.backend.service.fcm.DeviceTokenService;
import com.shopelec.backend.service.fcm.FcmService;
import com.shopelec.backend.service.order.OrderDetailService;
import com.shopelec.backend.service.order.OrderService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/v1/admin/order")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    UserService userService;
    OrderService orderService;
    OrderDetailService detailService;
    CouponsService couponsService;
    DeviceTokenService deviceTokenService;
    FcmService fcmService;

    @GetMapping
    public String toOrder(Model model,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "5") int size,
                          @RequestParam(required = false, defaultValue = "") String status) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse admin = userService.findByEmail(name);
        model.addAttribute("admin", admin);
        Pageable pageable = PageRequest.of(page, size);

        Page<Order> ordersPage;
        if (status != null && !status.isEmpty()) {
            ordersPage = orderService.getAllOrdersPaginatedStatus(status, pageable);
        } else {
            ordersPage = orderService.getAllOrdersPaginated(pageable);
        }

        model.addAttribute("orders", ordersPage.getContent());
        model.addAttribute("currentPage", ordersPage.getNumber());
        model.addAttribute("totalPages", ordersPage.getTotalPages());
        model.addAttribute("status", status);
        return "order";
    }

    @GetMapping("/approveOrder/{orderId}")
    public String approve(@RequestParam String userId, @PathVariable Long orderId, RedirectAttributes redirectAttributes) throws ExecutionException, FirebaseMessagingException, InterruptedException {

        String token = deviceTokenService.getTokenByUserId(userId).getToken();
        orderService.update(orderId, "Chờ giao hàng");
        try {
            fcmService.sendNotification(token, "Trạng thái đơn hàng", "Đơn hàng đã được duyệt và đang giao đến bạn.");
        } catch (Exception ignored) {

        }

        redirectAttributes.addFlashAttribute("message", "Duyệt đơn hàng thành công");
        return "redirect:/v1/admin/order/" + orderId;
    }

    @GetMapping("/cancel/{orderId}")
    public String cancelOrder(RedirectAttributes redirectAttributes, @PathVariable Long orderId) throws ExecutionException, FirebaseMessagingException, InterruptedException {
        Order order = orderService.findById(orderId);
        String userId = order.getUser().getId();
        String token = deviceTokenService.getTokenByUserId(userId).getToken();
        orderService.updateStatus(orderId,"Đã hủy");
        fcmService.sendNotification(token, "Trạng thái đơn hàng", "Đơn hàng đã bị hủy bởi quản trị viên.");
        redirectAttributes.addFlashAttribute("message", "Hủy đơn hàng thành công");
        return "redirect:/v1/admin/order";
    }

    @GetMapping("/{order_id}")
    public String toDetailOrder(@PathVariable Long order_id, Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse admin = userService.findByEmail(name);
        Order order = orderService.findById(order_id);

        if(order.getCoupon_id() != -1) {
            Coupons coupons = couponsService.findById(order.getCoupon_id());
            model.addAttribute("coupons", coupons);

        }
        List<OrderDetailResponse> orderDetailResponses = detailService.getAllOrderDetailByOrder(order_id);

        double totalOriginal = 0;
        double totalDiscount = 0;
        double totalPayment = 0;
        for(OrderDetailResponse response : orderDetailResponses) {
            totalOriginal += response.getQuantity() * response.getProductResponse().getPrice();
            totalDiscount += response.getProductResponse().getPrice() * response.getQuantity()
                    * response.getProductResponse().getDiscount() / 100 ;
        }
        totalPayment = totalOriginal - totalDiscount;

        model.addAttribute("admin", admin);
        model.addAttribute("order",order);
        model.addAttribute("details", orderDetailResponses);
        model.addAttribute("totalOriginal", totalOriginal);
        model.addAttribute("totalDiscount", totalDiscount);
        model.addAttribute("totalPayment", totalPayment);

        return "order_detail";
    }
}
