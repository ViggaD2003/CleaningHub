package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.entity.User;
import com.fpu.exe.cleaninghub.services.interfc.RatingService;
import com.fpu.exe.cleaninghub.utils.wapper.API;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final RatingService ratingService;

    @GetMapping("/get-highest-average-staff")
    public API.Response<?> getFiveHighestAverageStaff() {
        List<User> users = ratingService.getFiveUserHaveHighestAverageRating();
        return API.Response.success(users);
    }
}
