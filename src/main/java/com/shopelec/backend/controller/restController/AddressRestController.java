package com.shopelec.backend.controller.restController;

import com.shopelec.backend.dto.request.AddressRequest;
import com.shopelec.backend.dto.response.AddressResponse;
import com.shopelec.backend.model.Address;
import com.shopelec.backend.service.address.AddressService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/api/address")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AddressRestController {

    AddressService addressService;

    @PostMapping
    public ResponseEntity<?> saveAddress(AddressRequest address) {
        AddressResponse response = addressService.save(address);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getAllAddressByUserId(@PathVariable Long user_id) {
        List<AddressResponse> responses = addressService.findAllByUserId(user_id);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateAddressById(AddressRequest request) {
        AddressResponse response = addressService.update(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/delete/{address_id}")
    public ResponseEntity<?> deleteAddressById(@PathVariable Long address_id) {
        addressService.delete(address_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
