package com.fpu.exe.cleaninghub.services.impl;

import com.fpu.exe.cleaninghub.dto.request.NotificationRequest;
import com.fpu.exe.cleaninghub.entity.Notification;
import com.fpu.exe.cleaninghub.entity.User;
import com.fpu.exe.cleaninghub.exception.OperationNotPermittedException;
import com.fpu.exe.cleaninghub.repository.NotificationRepository;
import com.fpu.exe.cleaninghub.repository.UserRepository;
import com.fpu.exe.cleaninghub.services.interfc.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);
    NotificationRepository notificationRepository;
    UserRepository userRepository;

    public Notification saveNotification(NotificationRequest request) {
        try {
            User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new RuntimeException("not found user"));

            Notification notification = new Notification();
            notification.setMessage(request.message());
            notification.setType(request.type());
            notification.setUserId(user.getId());
            notification.setBookingId(request.bookingId());
            notification.setStatus(request.status());
            notification.setCreatedAt(LocalDateTime.now());
            notification.setExpiresAt(LocalDateTime.now().plusDays(7));
            return notificationRepository.save(notification);
        }catch (Exception e){
            throw new OperationNotPermittedException(e.getMessage());
        }
    }

    public List<Notification> findNotificationsByUserId(String email, boolean isUnread){
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("not found user"));

            return notificationRepository.findNotificationsByUserId(user.getId())
                    .stream()
                    .filter(x -> !isUnread || x.getStatus().equalsIgnoreCase("unread"))
                    .toList();
        }catch (Exception e){
            throw new OperationNotPermittedException(e.getMessage());
        }
    }

    public Notification updateNotification(Integer id, String status){
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setStatus(status);
        return notificationRepository.save(notification);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredNotifications() {
        LocalDateTime dateTime = LocalDateTime.now();
        notificationRepository.deleteExpiredNotifications(dateTime);
        log.info("Expired notifications deleted for date: " + dateTime);
    }
}
