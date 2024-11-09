package com.fpu.exe.cleaninghub.services.interfc;

import com.fpu.exe.cleaninghub.dto.request.NotificationRequest;
import com.fpu.exe.cleaninghub.entity.Notification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface NotificationService {
    Notification saveNotification(NotificationRequest request);
    List<Notification> findNotificationsByUserId(String email, boolean isUnread);
    Notification updateNotification(Integer id, String status);
    void deleteExpiredNotifications();
}
