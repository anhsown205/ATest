package com.blood_donation.blood_donation.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.blood_donation.blood_donation.dto.DonationEditDto;
import com.blood_donation.blood_donation.dto.DonationRegistrationDto;
import com.blood_donation.blood_donation.entity.BloodType;
import com.blood_donation.blood_donation.entity.DonationRegistration;
import com.blood_donation.blood_donation.repository.BloodTypeRepository;
import com.blood_donation.blood_donation.service.DonationService;

@Controller
@RequestMapping("/donations")
public class DonationController {

    @Autowired
    private DonationService donationService;

    @Autowired
    private BloodTypeRepository bloodTypeRepository;

    // Hiển thị form đăng ký hiến máu
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        List<BloodType> bloodTypes = bloodTypeRepository.findAll();

        model.addAttribute("registrationDto", new DonationRegistrationDto());
        model.addAttribute("bloodTypes", bloodTypes);

        return "member/donation-register-form";
    }

    // Xử lý đăng ký hiến máu
    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("registrationDto") DonationRegistrationDto dto,
                                      Principal principal,
                                      RedirectAttributes redirectAttributes) {
        try {
            donationService.createDonationRegistration(dto, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage",
                    "Đăng ký hiến máu thành công! Chúng tôi sẽ liên hệ với bạn sớm nhất.");
            return "redirect:/dashboard";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/donations/register";
        }
        
    }
    @GetMapping("/history")
    public String showDonationHistory(Model model, Principal principal) {
        // Lấy username của người dùng đang đăng nhập
        String username = principal.getName();
        // Gọi service để lấy danh sách lịch sử
        List<DonationRegistration> history = donationService.findDonationHistoryByUsername(username);

        model.addAttribute("donationHistory", history);

        return "member/donation-history";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, Principal principal, RedirectAttributes redirectAttributes) {
        Optional<DonationRegistration> registrationOpt = donationService.findRegistrationByIdAndUsername(id, principal.getName());

        if (registrationOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy hoặc không có quyền truy cập.");
            return "redirect:/donations/history";
        }

        DonationRegistration registration = registrationOpt.get();
        // Chuyển đổi từ Entity sang DTO để hiển thị trên form
        DonationEditDto dto = new DonationEditDto();
        dto.setId(registration.getId());
        dto.setPhone(registration.getPhone());
        dto.setAddress(registration.getAddress());
        dto.setProvince(registration.getProvince());
        dto.setAvailableDate(registration.getAvailableDate());

        model.addAttribute("editDto", dto);
        return "member/donation-edit-form";
    }

    // --- THÊM PHƯƠNG THỨC XỬ LÝ CẬP NHẬT ---
    @PostMapping("/update")
    public String processUpdate(@ModelAttribute("editDto") DonationEditDto dto, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            donationService.updateDonationRegistration(dto, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin đăng ký thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            // Nếu lỗi, trả về lại chính trang sửa đó
            return "redirect:/donations/edit/" + dto.getId();
        }
        return "redirect:/donations/history";
    }
}
