package com.shopelec.backend.service.address;

import com.shopelec.backend.dto.request.AddressRequest;
import com.shopelec.backend.dto.response.AddressResponse;
import com.shopelec.backend.mapper.UserMapper;
import com.shopelec.backend.model.Address;
import com.shopelec.backend.repository.AddressRepository;
import com.shopelec.backend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AddressServiceImpl implements AddressService{

    AddressRepository addressRepository;
    UserRepository userRepository;

    @Override
    public List<AddressResponse> findAllByUserId(String user_id) {

        List<AddressResponse> responses = new ArrayList<>();
        List<Address> addresses = addressRepository.findAllByUserId(user_id);
        for(Address address : addresses) {
            responses.add(AddressResponse
                    .builder()
                    .isSelected(address.isSelected())
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
    public AddressResponse update(AddressRequest addressRequest) {
        Address address = Address.builder()
                .user(userRepository.findById(addressRequest.getUser_id()).orElseThrow(
                        () -> new RuntimeException("User not found")
                ))
                .id(addressRequest.getId())
                .name(addressRequest.getName())
                .phoneNumber(addressRequest.getPhoneNumber())
                .address(addressRequest.getAddress())
                .isSelected(addressRequest.isSelected())
                .build();
        if(address.isSelected()) {
            List<Address> addresses = addressRepository.findAllByUserId(addressRequest.getUser_id());
            for (Address address1 : addresses) {
                if(!Objects.equals(address1.getId(), address.getId()) && address1.isSelected()) {
                    address1.setSelected(false);
                    addressRepository.save(address1);
                }
            }
        }
        addressRepository.save(address);
        return AddressResponse.builder()
                .user_id(address.getUser().getId())
                .name(address.getName())
                .id(address.getId())
                .address(address.getAddress())
                .phoneNumber(address.getPhoneNumber())
                .build();
    }

    @Override
    public AddressResponse save(AddressRequest request) {
//        log.info(request.toString());
        List<Address> addresses = addressRepository.findAllByUserId(request.getUser_id());
        boolean isSelectedFirst = addresses.isEmpty();
        Address address = addressRepository.save(Address
                .builder()
                .name(request.getName())
                .user(userRepository.findById(request.getUser_id()).orElseThrow(
                        () -> new RuntimeException("User not found"))
                )
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .isSelected(isSelectedFirst)
                .build());
        return AddressResponse
                .builder()
                .user_id(address.getUser().getId())
                .name(address.getName())
                .id(address.getId())
                .address(address.getAddress())
                .phoneNumber(address.getPhoneNumber())
                .build();
    }

    @Override
    public AddressResponse setActive(AddressRequest request) {
//        log.info(request.toString());
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
                    .isSelected(request.isSelected())
                    .build());

            List<Address> addresses = addressRepository.findAllByUserId(request.getUser_id());
            for (Address address1 : addresses) {
                if(!Objects.equals(address1.getId(), address.getId()) && address1.isSelected()) {
                    address1.setSelected(false);
                    addressRepository.save(address1);
                }
            }

            return AddressResponse
                    .builder()
                    .user_id(address.getUser().getId())
                    .name(address.getName())
                    .id(address.getId())
                    .address(address.getAddress())
                    .phoneNumber(address.getPhoneNumber())
                    .isSelected(address.isSelected())
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
