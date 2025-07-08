package com.blood_donation.blood_donation.repository;

import com.blood_donation.blood_donation.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {

    // Tìm các bài viết đã được xuất bản để hiển thị cho Guest và Member
    List<Blog> findByStatus(Blog.Status status);
    // Tìm các bài blog theo status, sắp xếp theo ngày tạo giảm dần
    // Pageable sẽ giúp chúng ta giới hạn số lượng bài viết muốn lấy (ví dụ: 3 bài đầu tiên)
    Page<Blog> findByStatusOrderByCreatedAtDesc(Blog.Status status, Pageable pageable);
}