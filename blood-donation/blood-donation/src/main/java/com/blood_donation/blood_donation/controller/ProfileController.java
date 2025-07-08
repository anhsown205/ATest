package com.blood_donation.blood_donation.controller;

import com.blood_donation.blood_donation.dto.UserProfileDto;
import com.blood_donation.blood_donation.entity.User;
import com.blood_donation.blood_donation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    // Hiển thị form hồ sơ
    @GetMapping
    public String showProfileForm(Model model, Principal principal) {
        // Lấy thông tin người dùng hiện tại
        User currentUser = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Không thể tìm thấy người dùng đã đăng nhập."));

        // Tạo DTO và điền thông tin từ Entity
        UserProfileDto profileDto = new UserProfileDto();
        profileDto.setFullName(currentUser.getFullName());
        profileDto.setEmail(currentUser.getEmail());
        profileDto.setNationalId(currentUser.getNationalId());

        model.addAttribute("profileDto", profileDto);
        model.addAttribute("username", currentUser.getUsername()); // Gửi cả username để hiển thị

        return "member/profile";
    }

    // Xử lý cập nhật hồ sơ
    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("profileDto") UserProfileDto profileDto,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {
        try {
            userService.updateUserProfile(principal.getName(), profileDto);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật hồ sơ thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/profile";
    }
}