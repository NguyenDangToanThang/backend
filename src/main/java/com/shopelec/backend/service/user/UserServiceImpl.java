package com.shopelec.backend.service.user;

import java.util.List;

import com.google.firebase.auth.FirebaseAuthException;
import com.shopelec.backend.dto.request.ChangePasswordRequest;
import com.shopelec.backend.dto.response.ProductResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService{

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public User save(SignupRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("User existed");
        } 
        else {
            LocalDateTime now = LocalDateTime.now();
            String date = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            User user = User.builder()
                    .id(request.getId())
                    .name(request.getName())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .date_created(date)
                    .role("USER")
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();
            return userRepository.save(user);
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
    public UserResponse findById(String id) {
//        log.info("ID: {}", id);
        Optional<User> user = userRepository.findById(id);
//        log.info("User: {}", user);
        return userMapper.toUserResponse(user.get());
    }

    @Override
    public void deleteUser(String email) throws FirebaseAuthException {
        if(userRepository.existsByEmail(email)) {
            User user = userRepository.findByEmail(email).orElseThrow(
                    () -> new NullPointerException("User not found")
            );
            userRepository.delete(user);
//            firebaseService.deleteUserFirebase(email);
        } else {
            throw new NullPointerException("User not found");
        }
    }

    @Override
    public boolean changePasswordAdmin(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Admin not found")
        );

        boolean checkPassword = passwordEncoder.matches(request.getOldPassword(), user.getPassword());

        if(checkPassword) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
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
