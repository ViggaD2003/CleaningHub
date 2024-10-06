package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.dto.response.CreateBookingResponseDTO;
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
        simpMessagingTemplate.convertAndSendToUser(bookingResponseDTO.getStaff().getEmail(),"/queue/notifications", bookingResponseDTO);
    }
}