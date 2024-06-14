package com.shopelec.backend.controller.restController;

import com.shopelec.backend.model.Address;
import com.shopelec.backend.model.User;
import com.shopelec.backend.service.address.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shopelec.backend.dto.request.SignupRequest;
import com.shopelec.backend.dto.request.UpdateUserRequest;
import com.shopelec.backend.dto.response.UserResponse;
import com.shopelec.backend.service.user.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@CrossOrigin(allowedHeaders ="*",methods = {RequestMethod.POST , RequestMethod.GET})
@RequestMapping("/v1/api")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerController {
    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
    UserService userService;
    AddressService addressService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(userService.save(request));
    }

    @GetMapping("/getInfo")
    public ResponseEntity<UserResponse> getInfoByEmail(@RequestParam String id) {
//        log.info("ID getInfo: {}", id);
        UserResponse userResponse = userService.findById(id);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/update")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.update(request));
    }


}
