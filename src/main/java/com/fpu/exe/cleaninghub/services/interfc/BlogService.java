package com.fpu.exe.cleaninghub.services.interfc;

import com.fpu.exe.cleaninghub.dto.request.CreateBlogRequest;
import com.fpu.exe.cleaninghub.dto.request.UpdateBlogRequest;
import com.fpu.exe.cleaninghub.dto.response.BlogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BlogService {
    Page<BlogResponse> getBlogs(String searchTerm, Pageable pageable);
    BlogResponse getBlogById(Integer id);
    BlogResponse createBlog(CreateBlogRequest request);
    void updateBlog(Integer blogId, UpdateBlogRequest request);
    void deleteBlog(Integer id);
    void updateImgBlog(Integer blogId, String img);
}
