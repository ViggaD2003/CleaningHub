package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.dto.request.CreateBlogRequest;
import com.fpu.exe.cleaninghub.dto.request.UpdateBlogRequest;
import com.fpu.exe.cleaninghub.dto.response.BlogResponse;
import com.fpu.exe.cleaninghub.entity.Blog;
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
    public ResponseEntity<?> getBlogs(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(defaultValue = "1") int pageIndex) {
        try {
            Pageable pageable = PageRequest.of(pageIndex - 1, 5);
            var blogs = blogService.getBlogs(searchTerm, pageable);
            return ResponseEntity.ok(API.Response.success(blogs));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createBlog(@RequestBody CreateBlogRequest request) {
        try {
            var blog = blogService.createBlog(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(API.Response.success(blog));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PatchMapping("update-img-blog")
    public ResponseEntity<?> updateImgBlog(@RequestParam("blogId") Integer blogId, @RequestBody String img) {
        try{
            blogService.updateImgBlog(blogId, img);
            return ResponseEntity.ok(API.Response.success("Update success !"));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getBlog(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(API.Response.success(blogService.getBlogById(id)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("update-blog")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateBlog(@RequestParam("blogId") Integer blogId, @RequestBody UpdateBlogRequest blogResponse) {
        try {
            blogService.updateBlog(blogId, blogResponse);
            return ResponseEntity.ok(API.Response.success("Update successfully !"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("delete-blog")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteBlog(@RequestParam("blogId") Integer blogId) {
        try {
            blogService.deleteBlog(blogId);
            return ResponseEntity.ok(API.Response.success("Delete successfully !"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
