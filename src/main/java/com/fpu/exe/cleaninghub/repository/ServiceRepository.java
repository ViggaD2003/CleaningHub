package com.fpu.exe.cleaninghub.repository;

import com.fpu.exe.cleaninghub.entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service,Integer> {

    @Query("SELECT s FROM Service s WHERE s.status = 'active' AND (s.name LIKE %:searchTerm% OR s.description LIKE %:searchTerm%)")
    Page<Service> GetAllService(String searchTerm, Pageable pageable);
    @EntityGraph(attributePaths = {"category"})
    Optional<Service> findById(int id);
    @Query("SELECT s.category.id, COUNT(s) FROM Service s GROUP BY s.category.id")
    List<Object[]> countServicesByCategory();

}
