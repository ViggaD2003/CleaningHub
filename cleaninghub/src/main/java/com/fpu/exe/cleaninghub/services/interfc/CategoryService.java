package com.fpu.exe.cleaninghub.services.interfc;

import com.fpu.exe.cleaninghub.dto.request.CategoryRequest;
import com.fpu.exe.cleaninghub.dto.response.CategoryResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    void createCategory(CategoryRequest createCategoryRequest);
    void updateCategory(int id, CategoryRequest updateCategoryRequest);
    Page<CategoryResponseDTO> getAllCategory(Pageable pageable);
}
