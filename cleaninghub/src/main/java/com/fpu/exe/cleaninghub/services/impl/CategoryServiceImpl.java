package com.fpu.exe.cleaninghub.services.impl;

import com.fpu.exe.cleaninghub.config.ModelMapperConfig;
import com.fpu.exe.cleaninghub.dto.request.CategoryRequest;
import com.fpu.exe.cleaninghub.dto.response.CategoryResponseDTO;
import com.fpu.exe.cleaninghub.dto.response.UserResponseDTO;
import com.fpu.exe.cleaninghub.entity.Category;
import com.fpu.exe.cleaninghub.entity.User;
import com.fpu.exe.cleaninghub.repository.CategoryRepository;
import com.fpu.exe.cleaninghub.services.interfc.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void createCategory(CategoryRequest createCategoryRequest) {
        categoryRepository.save(modelMapper.map(createCategoryRequest, Category.class));
    }

    @Override
    public void updateCategory(int id, CategoryRequest updateCategoryRequest) {
        Category existCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category is not existed"));
        modelMapper.map(updateCategoryRequest, existCategory);
        categoryRepository.save(existCategory);
    }

    @Override
    public Page<CategoryResponseDTO> getAllCategory(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(category -> modelMapper.map(category, CategoryResponseDTO.class));
    }

}
