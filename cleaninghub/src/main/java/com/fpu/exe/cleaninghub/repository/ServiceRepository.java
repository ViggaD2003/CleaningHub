package com.fpu.exe.cleaninghub.repository;

import com.fpu.exe.cleaninghub.entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Service,Integer> {

    @Query("SELECT s FROM Service s WHERE s.status = 'Available' AND (s.name LIKE %:searchTerm% OR s.description LIKE %:searchTerm%)")
    Page<Service> GetAllService(String searchTerm, Pageable pageable);
}
