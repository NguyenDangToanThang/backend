package com.shopelec.backend.service.address;

import com.shopelec.backend.dto.request.AddressRequest;
import com.shopelec.backend.dto.response.AddressResponse;
import com.shopelec.backend.model.Address;

import java.util.List;

public interface AddressService {
    List<AddressResponse> findAllByUserId(String user_id);
    AddressResponse save(AddressRequest address);
    AddressResponse setActive(AddressRequest address);
    void delete(Long id);
    AddressResponse update(AddressRequest addressRequest);
}
