package com.shopelec.backend.service.coupons;

import com.shopelec.backend.dto.response.CouponsResponse;
import com.shopelec.backend.model.Coupons;
import com.shopelec.backend.repository.CouponsRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CouponsServiceImpl implements CouponsService{

    CouponsRepository couponsRepository;

    @Override
    public List<CouponsResponse> getAllCouponsWithTotalPayment(double totalPayment) {
        List<CouponsResponse> responses = new ArrayList<>();
        List<Coupons> coupons = couponsRepository.findAll();
        if(coupons.isEmpty()) {
            return List.of();
        }
        for(Coupons item : coupons) {
            String status = "Đủ điều kiện";
            java.util.Date date = new Date();
            if(item.getExpiredDate().compareTo(date) < 0) {
                status = "Hết hạn";
            }
            else if(item.getQuantity() < 1) {
                status = "Đã hết mã";
            } else if (item.getDiscountLimit() > totalPayment) {
                status = "Không đủ điều kiện";
            }
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            if(status.equals("Đủ điều kiện")) {
                responses.add(CouponsResponse.builder()
                        .id(item.getId())
                        .code(item.getCode())
                        .description(item.getDescription())
                        .expiredDate(df.format(item.getExpiredDate()))
                        .quantity(item.getQuantity())
                        .discount(item.getDiscount())
                        .discountLimit(item.getDiscountLimit())
                        .status(status)
                        .build());
            }
        }
        return responses;
    }

    @Override
    public List<CouponsResponse> getAllCoupons() {
        List<CouponsResponse> responses = new ArrayList<>();
        List<Coupons> coupons = couponsRepository.findAll();
        if(coupons.isEmpty()) {
            return List.of();
        }
        for(Coupons item : coupons) {

            String status = "Còn mã";
            java.util.Date date = new Date();
            if(item.getExpiredDate().compareTo(date) < 0) {
                status = "Hết hạn";
            }
            else if(item.getQuantity() < 1) {
                status = "Đã hết mã";
            }
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            responses.add(CouponsResponse.builder()
                    .id(item.getId())
                    .code(item.getCode())
                    .description(item.getDescription())
                    .expiredDate(df.format(item.getExpiredDate()))
                    .quantity(item.getQuantity())
                    .discount(item.getDiscount())
                    .status(status)
                    .discountLimit(item.getDiscountLimit())
                    .build());
        }
        return responses;
    }

    @Override
    public CouponsResponse checkCoupons(String code,double totalPayment) {
        List<CouponsResponse> list = getAllCouponsWithTotalPayment(totalPayment);
        for(CouponsResponse couponsResponse : list) {
            if(Objects.equals(couponsResponse.getCode(), code)) {
                return couponsResponse;
            }
        }
        return null;
    }

    @Override
    public Coupons findById(Long id) {
        return couponsRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Coupons not found")
        );
    }

    @Override
    public void updateCoupons(CouponsResponse request) {
        Coupons coupons = couponsRepository.findById(request.getId()).orElseThrow(
                () -> new RuntimeException("Coupons not found")
        );
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(request.getExpiredDate(), formatter);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Coupons coupons1 = couponsRepository.save(
                Coupons.builder()
                        .id(request.getId())
                        .code(request.getCode())
                        .discountLimit(request.getDiscountLimit())
                        .discount(request.getDiscount())
                        .description(request.getDescription())
                        .quantity(request.getQuantity())
                        .expiredDate(date)
                        .build()
        );
    }

    @Override
    public void createCoupons(CouponsResponse request) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(request.getExpiredDate(), formatter);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Coupons coupons1 = couponsRepository.save(
                Coupons.builder()
                        .code(request.getCode())
                        .discountLimit(request.getDiscountLimit())
                        .discount(request.getDiscount())
                        .description(request.getDescription())
                        .quantity(request.getQuantity())
                        .expiredDate(date)
                        .build()
        );
    }
}
