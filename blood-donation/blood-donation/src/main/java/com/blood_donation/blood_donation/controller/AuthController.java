package com.blood_donation.blood_donation.controller;

import com.blood_donation.blood_donation.dto.UserRegistrationDto;
import com.blood_donation.blood_donation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // Hiển thị form đăng ký
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // Đưa một đối tượng DTO rỗng vào model để form có thể binding dữ liệu
        model.addAttribute("userDto", new UserRegistrationDto());
        return "register";
    }

    // Xử lý dữ liệu từ form đăng ký
    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("userDto") UserRegistrationDto userDto,
                                      RedirectAttributes redirectAttributes) {
        try {
            userService.registerNewUser(userDto);
            // Thêm một thuộc tính flash (chỉ tồn tại trong 1 request) để hiển thị thông báo thành công
            redirectAttributes.addFlashAttribute("successMessage", "Đăng ký tài khoản thành công! Vui lòng đăng nhập.");
            return "redirect:/login"; // Chuyển hướng về trang đăng nhập
        } catch (RuntimeException e) {
            // Nếu có lỗi (ví dụ: username tồn tại), thêm lỗi vào flash attribute
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register"; // Chuyển hướng về lại trang đăng ký
        }
    }
    
    // Hiển thị form đăng nhập
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}