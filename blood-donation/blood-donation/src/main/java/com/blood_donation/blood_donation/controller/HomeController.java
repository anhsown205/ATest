package com.blood_donation.blood_donation.controller;

import com.blood_donation.blood_donation.entity.Blog;
import com.blood_donation.blood_donation.entity.MedicalCenter;
import com.blood_donation.blood_donation.service.BlogService;
import com.blood_donation.blood_donation.service.MedicalCenterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private MedicalCenterService medicalCenterService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        // Lấy thông tin trung tâm y tế (giả sử chỉ có 1 và id là 1)
        // .orElse(null) để tránh lỗi nếu không tìm thấy
        MedicalCenter center = medicalCenterService.findPrimaryCenter();

        // Lấy 3 bài blog mới nhất
        List<Blog> latestBlogs = blogService.findLatestPublishedBlogs(3);

        // Đưa dữ liệu vào Model để View có thể sử dụng
        model.addAttribute("center", center);
        model.addAttribute("latestBlogs", latestBlogs);

        // Trả về tên của file view (index.html)
        return "index";
    }
}