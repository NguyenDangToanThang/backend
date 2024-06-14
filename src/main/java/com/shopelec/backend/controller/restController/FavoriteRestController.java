package com.shopelec.backend.controller.restController;

import com.shopelec.backend.dto.request.FavoriteRequest;
import com.shopelec.backend.dto.response.FavoriteResponse;
import com.shopelec.backend.service.product.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/api/favorite")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class FavoriteRestController {

    ProductService productService;

    @GetMapping("/delete")
    public ResponseEntity<?> deleteFavorite(@RequestParam String user_id, @RequestParam Long product_id) {
        productService.deleteFavorite(user_id,product_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getFavoriteProductByUserId(@RequestParam String user_id) {
        List<FavoriteResponse> responses = productService.findAllFavoriteByUserId(user_id);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> saveFavoriteByUserIdAndProductId(@RequestBody FavoriteRequest request) {
        FavoriteResponse response = productService.saveFavorite(request);
        if(response == null) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<FavoriteResponse>(response, HttpStatus.OK);
    }
}
