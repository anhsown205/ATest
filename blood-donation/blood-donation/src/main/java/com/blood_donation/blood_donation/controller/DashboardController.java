package com.blood_donation.blood_donation.controller;

import com.blood_donation.blood_donation.entity.User;
import com.blood_donation.blood_donation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class DashboardController {

    @Autowired
    private UserRepository userRepository; // Dùng để lấy thông tin chi tiết của user

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        // Principal chứa thông tin của người dùng đã đăng nhập, tên của nó chính là username
        String username = principal.getName();

        // Lấy toàn bộ thông tin User từ CSDL để hiển thị
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy người dùng đã đăng nhập"));

        // Đưa thông tin người dùng vào model
        model.addAttribute("loggedInUser", user);

        return "dashboard";
    }
}