package com.blood_donation.blood_donation.service;

import com.blood_donation.blood_donation.entity.Blog;
import com.blood_donation.blood_donation.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public List<Blog> findLatestPublishedBlogs(int limit) {
        // Tạo một đối tượng Pageable để yêu cầu trang đầu tiên (index 0) với số lượng 'limit'
        Pageable pageable = PageRequest.of(0, limit);
        // Gọi phương thức repository đã tạo ở bước 1
        return blogRepository.findByStatusOrderByCreatedAtDesc(Blog.Status.PUBLISHED, pageable).getContent();
    }
    @Override
    public Page<Blog> findAllPublished(Pageable pageable) {
        // Chỉ cần gọi phương thức repository đã có sẵn từ trước
        return blogRepository.findByStatusOrderByCreatedAtDesc(Blog.Status.PUBLISHED, pageable);
    }

    @Override
    public Optional<Blog> findPublishedById(Integer id) {
        return blogRepository.findById(id)
                .filter(blog -> blog.getStatus() == Blog.Status.PUBLISHED);
    }

}