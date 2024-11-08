package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.dto.request.CreateBlogRequest;
import com.fpu.exe.cleaninghub.services.interfc.BlogService;
import com.fpu.exe.cleaninghub.utils.wapper.API;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/blogs")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BlogController {
    BlogService blogService;

    @GetMapping
    public ResponseEntity<?> getBlogs(@RequestParam(defaultValue = "1") int pageIndex){
        try {
            Pageable pageable = PageRequest.of(pageIndex - 1, 10);
            var blogs = blogService.getBlogs(pageable);
            return ResponseEntity.ok(API.Response.success(blogs));
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createBlog(@RequestBody CreateBlogRequest request){
        try {
            var blog = blogService.createBlog(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(API.Response.success(blog));
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getBlog(@PathVariable Integer id){
        try {
            return ResponseEntity.ok(API.Response.success(blogService.getBlogById(id)));
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
