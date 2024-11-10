package com.fpu.exe.cleaninghub.services.impl;

import com.fpu.exe.cleaninghub.dto.request.CreateBlogRequest;
import com.fpu.exe.cleaninghub.dto.request.UpdateBlogRequest;
import com.fpu.exe.cleaninghub.dto.response.BlogResponse;
import com.fpu.exe.cleaninghub.entity.Blog;
import com.fpu.exe.cleaninghub.entity.Booking;
import com.fpu.exe.cleaninghub.entity.User;
import com.fpu.exe.cleaninghub.repository.BlogRepository;
import com.fpu.exe.cleaninghub.services.interfc.BlogService;
import javassist.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
    BlogRepository blogRepository;
    ModelMapper modelMapper;

    @Override
    public Page<BlogResponse> getBlogs(String searchTerm, Pageable pageable) {
        return blogRepository.findAllWithSearch(searchTerm, pageable).map(blog -> modelMapper.map(blog, BlogResponse.class));
    }

    @Override
    public BlogResponse getBlogById(Integer id) {
        var blog = blogRepository.findById(id).orElseThrow(() -> new RuntimeException("Blog not found"));
        return modelMapper.map(blog, BlogResponse.class);
    }

    @Override
    public BlogResponse createBlog(CreateBlogRequest request) {
        var blog = modelMapper.map(request, Blog.class);
        blogRepository.save(blog);
        return modelMapper.map(blog, BlogResponse.class);
    }


    @Override
    @Transactional
    public void updateBlog(Integer blogId, UpdateBlogRequest request) {
      Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new RuntimeException("Blog not found"));
      blog.setContent(request.getContent());
      blog.setTitle(request.getTitle());
      blogRepository.save(blog);
    }

    @Override
    public void deleteBlog(Integer id) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new RuntimeException("Blog not found"));
        if(blog != null){
            blogRepository.delete(blog);
        }
    }

    @Override
    public void updateImgBlog(Integer blogId, String img) {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new RuntimeException("Blog not found"));
       blog.setImg(img);
        blogRepository.save(blog);
    }
}
