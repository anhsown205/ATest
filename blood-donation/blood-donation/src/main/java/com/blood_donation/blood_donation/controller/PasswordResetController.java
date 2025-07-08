package com.blood_donation.blood_donation.controller;

import com.blood_donation.blood_donation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PasswordResetController {

    @Autowired
    private UserService userService;

    // Hiển thị form yêu cầu
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password-form";
    }

    // Xử lý yêu cầu
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String userEmail,
                                        RedirectAttributes redirectAttributes) {
        // Gọi service để đặt lại mật khẩu
        userService.resetPasswordToDefault(userEmail);

        // Luôn hiển thị thông báo chung chung để bảo mật
        redirectAttributes.addFlashAttribute("successMessage",
                "Nếu email của bạn tồn tại, mật khẩu đã được đặt lại thành '123456'. Vui lòng đăng nhập lại.");
        return "redirect:/login"; // Chuyển thẳng về trang đăng nhập
    }
}