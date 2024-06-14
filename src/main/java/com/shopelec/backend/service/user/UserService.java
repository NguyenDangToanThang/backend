package com.shopelec.backend.service.user;

import java.util.List;

import com.google.firebase.auth.FirebaseAuthException;
import com.shopelec.backend.dto.request.ChangePasswordRequest;
import com.shopelec.backend.dto.request.SigninRequest;
import com.shopelec.backend.dto.request.SignupRequest;
import com.shopelec.backend.dto.request.UpdateUserRequest;
import com.shopelec.backend.dto.response.UserResponse;
import com.shopelec.backend.model.User;

public interface UserService {
    User save(SignupRequest request);
    UserResponse update(UpdateUserRequest request);
    List<UserResponse> getAllUser();
    UserResponse findByEmail(String email);
    UserResponse findById(String id);

    void deleteUser(String email) throws FirebaseAuthException;
    boolean changePasswordAdmin(String email, ChangePasswordRequest request);

}
