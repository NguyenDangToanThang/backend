package com.shopelec.backend.controller.restController;

import com.shopelec.backend.dto.request.OrderRequest;
import com.shopelec.backend.dto.response.OrderDetailRatingResponse;
import com.shopelec.backend.dto.response.OrderDetailResponse;
import com.shopelec.backend.model.OrderDetail;
import com.shopelec.backend.service.order.OrderDetailService;
import com.shopelec.backend.service.order.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/api/order")
@CrossOrigin(allowedHeaders ="*",methods = {RequestMethod.POST , RequestMethod.GET})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class OrderRestController {

    OrderService orderService;
    OrderDetailService detailService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody OrderRequest request) {
        return new ResponseEntity<>(orderService.save(request),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getOrderStatus(@RequestParam String user_id, @RequestParam String status) {
        return new ResponseEntity<>(orderService.getAllOrderByUserId(user_id,status),HttpStatus.OK);
    }

    @GetMapping("/updateStatus")
    public ResponseEntity<?> updateStatus(@RequestParam Long orderId, @RequestParam String status) {
        orderService.updateStatus(orderId,status);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getOrderDetails")
    public ResponseEntity<?> getAllOrderDetails(@RequestParam Long orderId) {
        List<OrderDetailRatingResponse> responses = detailService.getAllOrderDetailByOrderId(orderId);
        return new ResponseEntity<>(responses,HttpStatus.OK);
    }

    @GetMapping("/detail/updateStatus")
    public ResponseEntity<?> updateDetailStatus(@RequestParam Long productId , @RequestParam Long orderId) {
        detailService.updateStatus(productId,orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
