package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.dto.request.NotificationRequest;
import com.fpu.exe.cleaninghub.entity.Notification;
import com.fpu.exe.cleaninghub.services.interfc.NotificationService;
import com.fpu.exe.cleaninghub.utils.wapper.API;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class NotificationController {
    NotificationService notificationService;

    @PostMapping
    public ResponseEntity<?> saveNotification(@RequestBody NotificationRequest request){
        return ResponseEntity.ok(API.Response.success(notificationService.saveNotification(request)));
    }
    @GetMapping
    public ResponseEntity<?> getNotificationsByUserId(@RequestParam String email, @RequestParam boolean isUnread){
        return ResponseEntity.ok(API.Response.success(notificationService.findNotificationsByUserId(email, isUnread)));
    }
    @PutMapping("{id}")
    public ResponseEntity<?> updateNotificationStatus(@PathVariable(name = "id") Integer id, @RequestParam String status){
        return ResponseEntity.ok(API.Response.success(notificationService.updateNotification(id, status)));
    }
}
