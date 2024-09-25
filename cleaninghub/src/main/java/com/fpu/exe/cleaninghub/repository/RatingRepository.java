package com.fpu.exe.cleaninghub.repository;

import com.fpu.exe.cleaninghub.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface RatingRepository extends JpaRepository<Rating, Integer> {

    @Query("SELECT r FROM Rating r WHERE r.booking.staff.id = :staffId")
    List<Rating> findAllByStaffId(int staffId);
}
