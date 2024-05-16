package com.shopelec.backend.service.user;

import java.util.List;

import com.shopelec.backend.dto.request.SigninRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopelec.backend.dto.request.SignupRequest;
import com.shopelec.backend.dto.request.UpdateUserRequest;
import com.shopelec.backend.dto.response.UserResponse;
import com.shopelec.backend.mapper.UserMapper;
import com.shopelec.backend.model.User;
import com.shopelec.backend.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService{

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public UserResponse save(SignupRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("User existed");
        } 
        else {
            User user = userMapper.toUser(request);
            LocalDateTime now = LocalDateTime.now();
            String date = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            user.setDate_created(date);
            user.setRole("USER");
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            return userMapper.toUserResponse(userRepository.save(user));
        }
        
    }

    @Override
    public List<UserResponse> getAllUser() {
        var users = userRepository.findAll();
        return users.stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
            () -> new RuntimeException("User not found")
        );
        return userMapper.toUserResponse(user);
    }

    @Override
    public User findByEmailAndPassword(SigninRequest request) {
        return userRepository.findByEmailAndPassword(request.getEmail(), passwordEncoder.encode(request.getPassword()));
    }

    @Override
    public UserResponse update(UpdateUserRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
            () -> new RuntimeException("User not found")
        );

        userMapper.updateUser(user,request);
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        user.setDate_updated(date);
        return userMapper.toUserResponse(userRepository.save(user));
    }
}
