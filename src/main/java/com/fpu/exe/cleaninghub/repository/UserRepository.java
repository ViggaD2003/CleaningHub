package com.fpu.exe.cleaninghub.repository;

import com.fpu.exe.cleaninghub.entity.Role;
import com.fpu.exe.cleaninghub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String userEmail);
    User findByRole(Role role);
    @Query(value = "SELECT u FROM User u ORDER BY u.averageRating DESC LIMIT 1")
    User findStaffByHighestAverageRating();
    @Query("SELECT c FROM User c WHERE c.role.id = 3")
    List<User> findAllStaff();
//    @Query("SELECT u\n" +
//            "FROM User u \n" +
//            "LEFT JOIN Booking b ON b.staff.id = u.id\n" +
//            "AND b.status != 'COMPLETED'\n" +
//            "AND b.startDate < :endTime\n" +
//            "AND b.endDate > :startTime\n" +
//            "WHERE u.role.id = 3\n" +
//            "AND b.id IS NULL\n" +
//            "AND u.accountLocked = true\n")
//    List<User> findStaffByBookingTime(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);


    @Query("SELECT u " +
            "FROM User u " +
            "LEFT JOIN u.assignOfStaffs b " +  // Join với bảng Booking thông qua assignOfStaffs
            "ON b.status != 'COMPLETED' " +  // Điều kiện trạng thái Booking chưa hoàn thành
            "AND b.startDate < :endTime " +  // Điều kiện thời gian không xung đột
            "AND b.endDate > :startTime " +
            "WHERE u.role.id = 3 " +  // Lọc theo role của User (staff)
            "AND b.id IS NULL " +  // Staff không có booking trùng thời gian
            "AND u.accountLocked = true")
    List<User> findStaffByBookingTime(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);


    @Query("SELECT u FROM User u WHERE u.role.id = 3 ORDER BY u.averageRating DESC LIMIT 5")
    List<User> getFiveUserHaveHighestAverageRating();
}
