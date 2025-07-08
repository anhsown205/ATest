package com.blood_donation.blood_donation.controller;

import com.blood_donation.blood_donation.dto.PasswordChangeDto;
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

    // === 1. HIỂN THỊ TRANG XEM HỒ SƠ CHÍNH ===
    @GetMapping
    public String showProfileViewPage(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "member/profile-view"; // Trả về trang chỉ xem
    }

    // === 2. HIỂN THỊ FORM CHỈNH SỬA THÔNG TIN ===
    @GetMapping("/edit")
    public String showProfileEditForm(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfileDto profileDto = new UserProfileDto();
        profileDto.setFullName(user.getFullName());
        profileDto.setEmail(user.getEmail());
        profileDto.setNationalId(user.getNationalId());
        profileDto.setDateOfBirth(user.getDateOfBirth()); // <-- THÊM DÒNG NÀY
        profileDto.setPhone(user.getPhone()); 
        profileDto.setPosition(user.getPosition());

        model.addAttribute("profileDto", profileDto);
        return "member/profile-edit-form"; // Trả về trang form sửa
    }

    // === 3. XỬ LÝ VIỆC CẬP NHẬT THÔNG TIN ===
    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("profileDto") UserProfileDto profileDto, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUserProfile(principal.getName(), profileDto);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/profile"; // Quay về trang xem hồ sơ chính
    }

    // === 4. HIỂN THỊ FORM ĐỔI MẬT KHẨU ===
    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("passwordChangeDto", new PasswordChangeDto());
        return "member/change-password-form"; // Trả về trang form đổi mật khẩu
    }

    // === 5. XỬ LÝ VIỆC ĐỔI MẬT KHẨU ===
    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute("passwordChangeDto") PasswordChangeDto dto,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        try {
            userService.changeUserPassword(principal.getName(), dto);
            redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/profile"; // Quay về trang xem hồ sơ chính
    }
}