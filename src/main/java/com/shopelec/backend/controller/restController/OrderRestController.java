package com.shopelec.backend.controller.restController;

import com.shopelec.backend.dto.request.OrderRequest;
import com.shopelec.backend.service.order.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/api/order")
@CrossOrigin(allowedHeaders ="*",methods = {RequestMethod.POST , RequestMethod.GET})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class OrderRestController {

    OrderService orderService;

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
}
