package com.fpu.exe.cleaninghub.repository;

import com.fpu.exe.cleaninghub.entity.Duration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DurationRepository extends JpaRepository<Duration, Integer> {
}
