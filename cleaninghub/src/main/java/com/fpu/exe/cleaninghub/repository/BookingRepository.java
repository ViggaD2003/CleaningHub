package com.fpu.exe.cleaninghub.repository;

import com.fpu.exe.cleaninghub.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("SELECT b FROM Booking b " +
            "WHERE b.user.id = :userId AND " +
            "(b.status LIKE %:searchTerm% OR " +
            "b.address.street LIKE %:searchTerm% OR " +
            "b.address.city LIKE %:searchTerm% OR " +
            "b.address.state LIKE %:searchTerm% OR " +
            "b.address.zipCode LIKE %:searchTerm% OR " +
            "b.service.name LIKE %:searchTerm%)")
    Page<Booking> findByUserId(Integer userId, String searchTerm, Pageable pageable);
    Optional<Booking> findByIdAndUserId(Integer bookingId, Integer userId);
    Page<Booking> findByStaffId(Integer staffId, Pageable pageable);
}