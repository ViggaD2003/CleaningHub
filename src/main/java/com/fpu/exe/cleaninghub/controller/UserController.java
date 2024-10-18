package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.dto.request.LocationRequest;
import com.fpu.exe.cleaninghub.entity.User;
import com.fpu.exe.cleaninghub.services.interfc.RatingService;
import com.fpu.exe.cleaninghub.services.interfc.UserService;
import com.fpu.exe.cleaninghub.utils.wapper.API;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final RatingService ratingService;

    private final UserService userService;

    @GetMapping("/get-highest-average-staff")
    public API.Response<?> getFiveHighestAverageStaff() {
        List<User> users = ratingService.getFiveUserHaveHighestAverageRating();
        return API.Response.success(users);
    }


    @PatchMapping("/update-location-staff")
    public API.Response<?> updateLocationStaff(HttpServletRequest request, @RequestBody LocationRequest locationRequest) {
        String response = userService.updateLocationOfStaff(request, locationRequest);
        return API.Response.success(response);
    }
}
