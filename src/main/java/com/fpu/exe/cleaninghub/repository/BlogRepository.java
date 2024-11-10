package com.fpu.exe.cleaninghub.repository;

import com.fpu.exe.cleaninghub.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BlogRepository extends JpaRepository<Blog, Integer> {
    @Query("SELECT b FROM Blog b WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Blog> findAllWithSearch(@Param("keyword") String keyword, Pageable pageable);

}
