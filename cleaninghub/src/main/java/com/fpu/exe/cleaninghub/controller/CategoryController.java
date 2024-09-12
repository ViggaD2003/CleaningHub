package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.dto.request.CategoryRequest;
import com.fpu.exe.cleaninghub.dto.response.CategoryResponseDTO;
import com.fpu.exe.cleaninghub.services.interfc.CategoryService;
import com.fpu.exe.cleaninghub.utils.wapper.API;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    @Autowired
    private final CategoryService categoryService;
    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest){
        try{
            categoryService.createCategory(categoryRequest);
            return ResponseEntity.ok(API.Response.success("Create Category successfully"));
        }catch (Exception e){
            return ResponseEntity.ok(API.Response.error(HttpStatus.BAD_REQUEST, "Something went wrong!!", e.getMessage()));
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable int id, @RequestBody CategoryRequest categoryRequest){
        try{
            categoryService.updateCategory(id, categoryRequest);
            return ResponseEntity.ok(API.Response.success("Update Category successfully"));
        }catch (Exception e){
            return ResponseEntity.ok(API.Response.error(HttpStatus.BAD_REQUEST, "Something went wrong!!", e.getMessage()));
        }
    }
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllCategories(@RequestParam(defaultValue = "0") Integer page,
                                            @RequestParam(defaultValue = "10") Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryResponseDTO> categories = categoryService.getAllCategory(pageable);
        return ResponseEntity.ok(categories);
    }
}
