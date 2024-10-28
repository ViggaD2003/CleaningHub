package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.dto.request.AccountRequest;
import com.fpu.exe.cleaninghub.dto.request.LocationRequest;
import com.fpu.exe.cleaninghub.dto.response.AccountResponseDto;
import com.fpu.exe.cleaninghub.dto.response.UserResponseDTO;
import com.fpu.exe.cleaninghub.entity.User;
import com.fpu.exe.cleaninghub.services.interfc.RatingService;
import com.fpu.exe.cleaninghub.services.interfc.UserService;
import com.fpu.exe.cleaninghub.utils.wapper.API;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/users")
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

    @GetMapping()
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @RequestParam(required = false, defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<UserResponseDTO> users = userService.getAllUsers(searchTerm, page, size);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}/update-role-status")
    public ResponseEntity<User> updateUserRoleAndStatus(
            @PathVariable Integer userId,
            @RequestParam Integer roleId,
            @RequestParam Boolean status) {
        User updatedUser = userService.updateUserRoleAndStatus(userId, roleId, status);
        return ResponseEntity.ok(updatedUser);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update-role")
    public ResponseEntity<String> updateRole(@RequestBody AccountRequest request) {
        boolean updated = userService.updateRole(request);
        if (updated) {
            return ResponseEntity.ok("User role updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Failed to update user role.");
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteAccount(@PathVariable Integer userId) {
        userService.deleteAccount(userId);
        return ResponseEntity.ok("User account deleted successfully.");
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/non-admin")
    public ResponseEntity<Page<AccountResponseDto>> getAllNonAdminAccounts(
            @RequestParam(defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AccountResponseDto> users = userService.getAllNonAdminAccounts(searchTerm, page, size);
        return ResponseEntity.ok(users);
    }
}
