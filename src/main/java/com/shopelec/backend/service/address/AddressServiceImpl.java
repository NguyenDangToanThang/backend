package com.shopelec.backend.service.address;

import com.shopelec.backend.dto.request.AddressRequest;
import com.shopelec.backend.dto.response.AddressResponse;
import com.shopelec.backend.dto.response.UserResponse;
import com.shopelec.backend.mapper.UserMapper;
import com.shopelec.backend.model.Address;
import com.shopelec.backend.repository.AddressRepository;
import com.shopelec.backend.repository.UserRepository;
import com.shopelec.backend.service.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AddressServiceImpl implements AddressService{

    AddressRepository addressRepository;
    UserRepository userRepository;
    UserMapper userMapper;

    @Override
    public List<AddressResponse> findAllByUserId(Long user_id) {

        List<AddressResponse> responses = new ArrayList<>();
        List<Address> addresses = addressRepository.findAllByUserId(user_id);
        for(Address address : addresses) {
            responses.add(AddressResponse
                    .builder()
                    .id(address.getId())
                    .address(address.getAddress())
                    .phoneNumber(address.getPhoneNumber())
                    .name(address.getName())
                    .user_id(address.getUser().getId())
                    .build());
        }
        return responses;
    }

    @Override
    public AddressResponse save(AddressRequest request) {
        Address address = addressRepository.save(Address
                .builder()
                .name(request.getName())
                .user(userRepository.findById(request.getUser_id()).orElseThrow(
                        () -> new RuntimeException("User not found"))
                )
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .build());
        return AddressResponse
                .builder()
                .user_id(address.getUser().getId())
                .name(address.getName())
                .id(address.getId())
                .address(address.getAddress())
                .build();
    }

    @Override
    public AddressResponse update(AddressRequest request) {
        if(addressRepository.existsById(request.getId())) {
            Address address = addressRepository.save(Address
                    .builder()
                    .id(request.getId())
                    .name(request.getName())
                    .user(userRepository.findById(request.getUser_id()).orElseThrow(
                            () -> new RuntimeException("User not found"))
                    )
                    .address(request.getAddress())
                    .phoneNumber(request.getPhoneNumber())
                    .build());
            return AddressResponse
                    .builder()
                    .user_id(address.getUser().getId())
                    .name(address.getName())
                    .id(address.getId())
                    .address(address.getAddress())
                    .build();
        }
        else {
            throw new RuntimeException("Address not found");
        }
    }

    @Override
    public void delete(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Address not found")
        );
        addressRepository.delete(address);
        log.info("Delete address id {} successful",id);
    }
}
