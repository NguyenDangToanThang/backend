package com.shopelec.backend.controller.restController;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shopelec.backend.dto.response.CouponsResponse;
import com.shopelec.backend.service.coupons.CouponsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/coupons")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(allowedHeaders ="*",methods = {RequestMethod.POST , RequestMethod.GET})
public class CouponsRestController {

    CouponsService couponsService;

    @GetMapping
    public ResponseEntity<?> getAllCoupons(@RequestParam(required = false, defaultValue = "-1") double totalPayment) {
        if(totalPayment != -1) {
            List<CouponsResponse> responses = couponsService.getAllCouponsWithTotalPayment(totalPayment);
            if(responses.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(responses,HttpStatus.OK);
        }
//        List<CouponsResponse> responses = couponsService.getAllCoupons();
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping("/checkCoupons")
    public ResponseEntity<?> checkCoupons(@RequestParam double totalPayment, @RequestParam String code) throws JsonProcessingException {
        code = code.toUpperCase();
        CouponsResponse response = couponsService.checkCoupons(code,totalPayment);
        if(response != null) {
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        String message = "Mã giảm giá không tồn tại";
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("message", message);
        return ResponseEntity.ok(jsonNode);
    }

}
