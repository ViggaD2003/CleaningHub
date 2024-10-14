package com.fpu.exe.cleaninghub.repository;

import com.fpu.exe.cleaninghub.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface RatingRepository extends JpaRepository<Rating, Integer> {

    @Query("SELECT r FROM Rating r " +
            "JOIN r.booking b " +  // Join vào booking từ Rating
            "JOIN b.staff s " +    // Join vào staff thông qua quan hệ ManyToMany của Booking
            "WHERE s.id = :staffId")
    List<Rating> findAllByStaffId(int staffId);
}
