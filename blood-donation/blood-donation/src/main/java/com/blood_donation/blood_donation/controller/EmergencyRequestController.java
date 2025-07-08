package com.blood_donation.blood_donation.controller;

import com.blood_donation.blood_donation.dto.EmergencyRequestDto;
import com.blood_donation.blood_donation.repository.BloodTypeRepository;
import com.blood_donation.blood_donation.service.EmergencyRequestService;
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
@RequestMapping("/requests/emergency")
public class EmergencyRequestController {

    @Autowired
    private EmergencyRequestService emergencyRequestService;
    @Autowired
    private BloodTypeRepository bloodTypeRepository;

    @GetMapping("/new")
    public String showNewRequestForm(Model model) {
        model.addAttribute("requestDto", new EmergencyRequestDto());
        model.addAttribute("bloodTypes", bloodTypeRepository.findAll());
        return "member/emergency-request-form";
    }

    @PostMapping
    public String createNewRequest(@ModelAttribute("requestDto") EmergencyRequestDto dto,
                                 Principal principal, RedirectAttributes redirectAttributes) {
        try {
            emergencyRequestService.createEmergencyRequest(dto, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Đã tạo yêu cầu máu khẩn cấp thành công. Vui lòng chờ Admin phê duyệt.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/dashboard";
    }
}