package com.shopelec.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.shopelec.backend.dto.request.SignupRequest;
import com.shopelec.backend.dto.request.UpdateUserRequest;
import com.shopelec.backend.dto.response.UserResponse;
import com.shopelec.backend.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(SignupRequest request);
    void updateUser(@MappingTarget User user,UpdateUserRequest request);
    UserResponse toUserResponse(User user);
}