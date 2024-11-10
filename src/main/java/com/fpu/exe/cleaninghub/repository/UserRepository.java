package com.fpu.exe.cleaninghub.repository;

import com.fpu.exe.cleaninghub.entity.Role;
import com.fpu.exe.cleaninghub.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

//    @Query("SELECT u " +
//            "FROM User u " +
//            "LEFT JOIN u.assignOfStaffs b " +  // Join với bảng Booking thông qua assignOfStaffs
//            "ON b.status != 'COMPLETED' " +  // Điều kiện trạng thái Booking chưa hoàn thành
//            "AND b.startDate < :endTime " +  // Điều kiện thời gian không xung đột
//            "AND b.endDate > :startTime " +
//            "WHERE u.role.id = 3 " +  // Lọc theo role của User (staff)
//            "AND b.id IS NULL " +  // Staff không có booking trùng thời gian
//            "AND u.accountLocked = true")
//    List<User> findStaffByBookingTime(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT u FROM User u LEFT JOIN u.assignOfStaffs b " +
            "WHERE u.role.id = 3 " +
            "AND (b IS NULL OR (b.status != 'COMPLETED' AND " +
            "(b.endDate <= :startTime OR b.startDate >= :endTime))) " +
            "AND u.accountLocked = true")
    List<User> findStaffByBookingTime(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);


    @Query("SELECT u FROM User u WHERE u.role.id = 3 ORDER BY u.averageRating DESC LIMIT 5")
    List<User> getFiveUserHaveHighestAverageRating();

    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "AND u.status = true")
    Page<User> searchUsers(String searchTerm, Pageable pageable);
    @Query("SELECT u FROM User u WHERE u.role.name != 'ROLE_ADMIN' AND u.status = true " +
            "AND (LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<User> findAllNonAdminUsers(String searchTerm, Pageable pageable);
}
