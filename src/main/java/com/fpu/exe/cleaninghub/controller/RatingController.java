package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.dto.request.RatingRequest;
import com.fpu.exe.cleaninghub.dto.response.RatingDTO;
import com.fpu.exe.cleaninghub.services.interfc.RatingService;
import com.fpu.exe.cleaninghub.utils.wapper.API;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ratings")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RatingController {
    RatingService ratingService;

    @PostMapping
    public ResponseEntity<?> createRating(@RequestBody RatingRequest ratingRequest){
        RatingDTO ratingDTO = ratingService.createRating(ratingRequest);
        return ResponseEntity.ok(API.Response.success(ratingDTO));
    }
}
