package com.fpu.exe.cleaninghub.repository;

import com.fpu.exe.cleaninghub.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findNotificationsByUserId(Integer userId);
    @Transactional
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.expiresAt =:currentDate")
    void deleteExpiredNotifications(LocalDateTime currentDate);
}
