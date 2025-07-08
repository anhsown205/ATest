package com.blood_donation.blood_donation.service;

import com.blood_donation.blood_donation.entity.Blog;
import org.springframework.data.domain.Page; // Thêm import
import org.springframework.data.domain.Pageable; // Thêm import
import java.util.List;
import java.util.Optional;

public interface BlogService {
    List<Blog> findLatestPublishedBlogs(int limit);
    Page<Blog> findAllPublished(Pageable pageable);
    Optional<Blog> findPublishedById(Integer id);
}