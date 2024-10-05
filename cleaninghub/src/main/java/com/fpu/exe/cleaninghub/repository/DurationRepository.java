package com.fpu.exe.cleaninghub.repository;

import com.fpu.exe.cleaninghub.entity.Duration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DurationRepository extends JpaRepository<Duration, Integer> {
    Optional<Duration> findByIdAndServiceId(Integer id, Integer serviceId);
    List<Duration> findByServiceId(Integer serviceId);
}
