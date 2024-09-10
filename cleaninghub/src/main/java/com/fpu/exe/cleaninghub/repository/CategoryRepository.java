package com.fpu.exe.cleaninghub.repository;

import com.fpu.exe.cleaninghub.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
