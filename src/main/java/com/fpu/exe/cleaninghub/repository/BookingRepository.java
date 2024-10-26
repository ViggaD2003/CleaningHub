package com.fpu.exe.cleaninghub.repository;

import com.fpu.exe.cleaninghub.entity.Booking;
import com.fpu.exe.cleaninghub.enums.Booking.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.List;
@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("SELECT b FROM Booking b " +
            "WHERE b.user.id = :userId AND " +
            "(COALESCE(:searchTerm, '') = '' OR " +
            "b.address LIKE %:searchTerm% OR " +
            "b.service.name LIKE %:searchTerm%)")
    Page<Booking> findByUserId(Integer userId, @Param("searchTerm") String searchTerm, Pageable pageable);



    Optional<Booking> findByIdAndUserId(Integer bookingId, Integer userId);
    @Query("SELECT b FROM Booking b " +
            "JOIN b.staff s " +
            "WHERE s.id = :staffId " +
            "AND b.status = :bookingStatus")
    Page<Booking> findByStaffId(@Param("staffId") Integer staffId,
                                @Param("bookingStatus") BookingStatus bookingStatus,
                                Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.staff s " +
            "WHERE s.id = :staffId " +
            "AND b.status = 'PENDING' ")
    List<Booking> findByStaffIdWithStatusPending(Integer staffId);
    @Query("SELECT b FROM Booking b " +
            "JOIN FETCH b.staff s " +
            "WHERE s.id = :staffId " +
            "AND b.id = :bookingId")
    Optional<Booking> findBookingDetailByStaffId(Integer bookingId, Integer staffId);
}
