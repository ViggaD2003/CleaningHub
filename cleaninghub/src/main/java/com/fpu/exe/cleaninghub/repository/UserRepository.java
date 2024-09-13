package com.fpu.exe.cleaninghub.repository;

import com.fpu.exe.cleaninghub.entity.Role;
import com.fpu.exe.cleaninghub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String userEmail);
    User findByRole(Role role);
    @Query(value = "SELECT u FROM User u ORDER BY u.averageRating DESC LIMIT 1")
    User findStaffByHighestAverageRating();
}
