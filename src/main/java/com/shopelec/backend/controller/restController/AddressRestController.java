package com.shopelec.backend.controller.restController;

import com.shopelec.backend.dto.request.AddressRequest;
import com.shopelec.backend.dto.response.AddressResponse;
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
@RequestMapping(value = "/v1/api/address")
@CrossOrigin(allowedHeaders ="*",methods = {RequestMethod.POST , RequestMethod.GET})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AddressRestController {

    AddressService addressService;

    @PostMapping
    public ResponseEntity<?> saveAddress(@RequestBody AddressRequest address) {
        AddressResponse response = addressService.save(address);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getAllAddressByUserId(@PathVariable String user_id) {
        List<AddressResponse> responses = addressService.findAllByUserId(user_id);
//        log.info(responses.toString());
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/getActive/{user_id}")
    public ResponseEntity<?> getAddressActive(@PathVariable String user_id) {
        List<AddressResponse> responses = addressService.findAllByUserId(user_id);
        AddressResponse addressResponse = null;
        for(AddressResponse response : responses) {
            if(response.isSelected()) {
                addressResponse = response;
            }
        }
        return new ResponseEntity<>(addressResponse, HttpStatus.OK);
    }

    @PostMapping("/setActive")
    public ResponseEntity<?> setActive(@RequestBody AddressRequest request) {
//        log.info(request.toString());
        AddressResponse response = addressService.setActive(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/delete/{address_id}")
    public ResponseEntity<?> deleteAddressById(@PathVariable Long address_id) {
        addressService.delete(address_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateAddressById(@RequestBody AddressRequest request) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
