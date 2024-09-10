package com.fpu.exe.cleaninghub.repository;

import com.fpu.exe.cleaninghub.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
