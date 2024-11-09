package com.fpu.exe.cleaninghub.services.impl;

import com.fpu.exe.cleaninghub.dto.request.CreateBlogRequest;
import com.fpu.exe.cleaninghub.dto.response.BlogResponse;
import com.fpu.exe.cleaninghub.entity.Blog;
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

import java.util.Optional;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
    BlogRepository blogRepository;
    ModelMapper modelMapper;

    @Override
    public Page<BlogResponse> getBlogs(Pageable pageable) {
        return blogRepository.findAll(pageable).map(blog -> modelMapper.map(blog, BlogResponse.class));
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
}
