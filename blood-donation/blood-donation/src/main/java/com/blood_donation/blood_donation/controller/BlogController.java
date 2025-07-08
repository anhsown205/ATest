package com.blood_donation.blood_donation.controller;

import com.blood_donation.blood_donation.entity.Blog;
import com.blood_donation.blood_donation.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable; // Thêm import
import java.util.Optional;

@Controller
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/blogs")
    public String listBlogs(@RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "5") int size,
                          Model model) {
        // Tạo đối tượng Pageable để truy vấn
        Pageable pageable = PageRequest.of(page, size);
        // Gọi service để lấy dữ liệu phân trang
        Page<Blog> blogPage = blogService.findAllPublished(pageable);

        // Đưa đối tượng Page vào model
        model.addAttribute("blogPage", blogPage);

        return "blogs"; // Tên của file view
    }

    @GetMapping("/blogs/{id}")
    public String viewBlogPost(@PathVariable("id") Integer id, Model model) {
        Optional<Blog> blogOptional = blogService.findPublishedById(id);
        if (blogOptional.isPresent()) {
            model.addAttribute("blog", blogOptional.get());
            return "blog-detail"; // Tên file view chi tiết
        } else {
            // Nếu không tìm thấy bài viết, có thể chuyển hướng về trang lỗi 404
            return "error/404";
        }
    }   
}
