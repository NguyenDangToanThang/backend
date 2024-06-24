package com.shopelec.backend.service.coupons;

import com.shopelec.backend.dto.response.CouponsResponse;
import com.shopelec.backend.model.Coupons;

import java.util.List;

public interface CouponsService {
    List<CouponsResponse> getAllCouponsWithTotalPayment(double totalPayment);
    List<CouponsResponse> getAllCoupons();
    CouponsResponse checkCoupons(String code,double totalPayment);
    Coupons findById(Long id);
    void updateCoupons(CouponsResponse request);
    void createCoupons(CouponsResponse request);
}
