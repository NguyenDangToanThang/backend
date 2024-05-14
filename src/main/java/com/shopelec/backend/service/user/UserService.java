package com.shopelec.backend.service.user;

import java.util.List;

import com.shopelec.backend.dto.request.SignupRequest;
import com.shopelec.backend.dto.request.UpdateUserRequest;
import com.shopelec.backend.dto.response.UserResponse;

public interface UserService {
    UserResponse save(SignupRequest request);
    UserResponse update(UpdateUserRequest request);
    List<UserResponse> getAllUser();
    UserResponse findByEmail(String email);
}
