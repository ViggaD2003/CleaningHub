package com.fpu.exe.cleaninghub.services.impl;

import com.fpu.exe.cleaninghub.dto.request.CategoryRequest;
import com.fpu.exe.cleaninghub.dto.response.CategoryResponseDTO;
import com.fpu.exe.cleaninghub.dto.response.UserResponseDTO;
import com.fpu.exe.cleaninghub.entity.Category;
import com.fpu.exe.cleaninghub.entity.User;
import com.fpu.exe.cleaninghub.repository.CategoryRepository;
import com.fpu.exe.cleaninghub.services.interfc.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void createCategory(CategoryRequest createCategoryRequest) {
        Category category = Category.builder()
                .name(createCategoryRequest.getName())
                .description(createCategoryRequest.getDescription())
                .build();
        categoryRepository.save(category);
    }

    @Override
    public void updateCategory(int id, CategoryRequest updateCategoryRequest) {
        Category existCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category is not existed"));
        existCategory.setName(updateCategoryRequest.getName());
        existCategory.setDescription(updateCategoryRequest.getDescription());
        categoryRepository.save(existCategory);
    }

    @Override
    public Page<CategoryResponseDTO> getAllCategory(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(CategoryResponseDTO :: fromCategory);
    }

}
