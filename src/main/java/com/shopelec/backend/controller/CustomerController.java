package com.shopelec.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(SignupRequest request) {
        return ResponseEntity.ok(userService.save(request));
    }

    @PostMapping("/getInfo")
    public ResponseEntity<UserResponse> getInfoByEmail(@RequestBody String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @PostMapping("/update")
    public ResponseEntity<UserResponse> updateUser(UpdateUserRequest request) {
        return ResponseEntity.ok(userService.update(request));
    }
    


}
