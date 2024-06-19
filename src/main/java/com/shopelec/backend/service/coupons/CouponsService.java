package com.shopelec.backend.service.coupons;

import com.shopelec.backend.dto.response.CouponsResponse;

import java.util.List;

public interface CouponsService {
    List<CouponsResponse> getAllCouponsWithTotalPayment(double totalPayment);
    List<CouponsResponse> getAllCoupons();
    CouponsResponse checkCoupons(String code,double totalPayment);
}
