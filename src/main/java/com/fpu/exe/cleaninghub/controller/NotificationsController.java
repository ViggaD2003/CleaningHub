package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.dto.response.BookingDetailStaffResponse;
import com.fpu.exe.cleaninghub.dto.response.CreateBookingResponseDTO;
import com.fpu.exe.cleaninghub.dto.response.UserResponseDTO;
import com.fpu.exe.cleaninghub.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationsController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/notifications")
    public void sendBookingToStaff(@Payload CreateBookingResponseDTO bookingResponseDTO){
        for(UserResponseDTO staff : bookingResponseDTO.getStaff()){
            simpMessagingTemplate.convertAndSendToUser(staff.getEmail(),"/queue/notifications", bookingResponseDTO);
        }
    }

    @MessageMapping("/feedbacks")
    public void sendNotificationFeedbackToUser(@Payload BookingDetailStaffResponse bookingDetailStaffResponse){
        simpMessagingTemplate.convertAndSendToUser(bookingDetailStaffResponse.getUser().getEmail(),"/topic/feedbacks", bookingDetailStaffResponse);
    }
}