package com.fpu.exe.cleaninghub.repository;

import com.fpu.exe.cleaninghub.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Integer> {
}
