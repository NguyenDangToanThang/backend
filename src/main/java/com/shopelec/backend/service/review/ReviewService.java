package com.shopelec.backend.service.review;

import com.shopelec.backend.dto.request.ReviewRequest;

public interface ReviewService {
    void save(ReviewRequest request);
}
