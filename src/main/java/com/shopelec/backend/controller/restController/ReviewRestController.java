package com.shopelec.backend.controller.restController;

import com.shopelec.backend.dto.request.ReviewRequest;
import com.shopelec.backend.service.review.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(allowedHeaders ="*",methods = {RequestMethod.POST , RequestMethod.GET})
@RequestMapping(value = "/v1/api/review")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewRestController {

    ReviewService reviewService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ReviewRequest request) {
        reviewService.save(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
