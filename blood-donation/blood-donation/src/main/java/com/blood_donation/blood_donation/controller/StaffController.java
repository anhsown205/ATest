package com.blood_donation.blood_donation.controller;

import com.blood_donation.blood_donation.entity.DonationRegistration;
import com.blood_donation.blood_donation.entity.EmergencyRequest;
import com.blood_donation.blood_donation.service.RequestService;
import com.blood_donation.blood_donation.service.StaffDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private StaffDashboardService staffDashboardService;

    @Autowired
    private RequestService requestService;

    // --- DASHBOARD ---
    @GetMapping("/dashboard")
    public String showStaffDashboard(Model model) {
        model.addAttribute("pendingRequests", staffDashboardService.getPendingEmergencyRequestCount());
        model.addAttribute("pendingDonors", staffDashboardService.getPendingDonationRegistrationCount());
        model.addAttribute("inventorySummary", staffDashboardService.getInventorySummary());
        model.addAttribute("recentRequests", staffDashboardService.getRecentPendingEmergencyRequests(5));

        return "staff/dashboard";
    }

    // --- QUẢN LÝ CÁC YÊU CẦU CHỜ (PENDING) ---
    @GetMapping("/requests")
    public String showRequestManagementPage(
            @RequestParam(name = "emergencyPage", defaultValue = "0") int emergencyPage,
            @RequestParam(name = "donorPage", defaultValue = "0") int donorPage,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        Page<EmergencyRequest> emergencyRequestPage = requestService.findPendingEmergencyRequests(PageRequest.of(emergencyPage, size));
        Page<DonationRegistration> donationRegistrationPage = requestService.findPendingDonationRegistrations(PageRequest.of(donorPage, size));

        model.addAttribute("emergencyRequestPage", emergencyRequestPage);
        model.addAttribute("donationRegistrationPage", donationRegistrationPage);

        return "staff/request-management";
    }

    // --- QUẢN LÝ NGƯỜI HIẾN MÁU ĐÃ DUYỆT (APPROVED) ---
    @GetMapping("/donors/approved")
    public String showApprovedDonorsPage(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<DonationRegistration> approvedDonorsPage = requestService.findApprovedDonationRegistrations(pageable);
        model.addAttribute("approvedDonorsPage", approvedDonorsPage);

        return "staff/approved-donors";
    }


    // --- CÁC HÀNH ĐỘNG (ACTIONS) ---

    @PostMapping("/donations/{id}/approve")
    public String approveDonation(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            requestService.approveDonationRegistration(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã chấp thuận yêu cầu hiến máu thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/staff/requests";
    }

    @PostMapping("/donations/{id}/reject")
    public String rejectDonation(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            requestService.rejectDonationRegistration(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã từ chối yêu cầu hiến máu.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/staff/requests";
    }

    @PostMapping("/donations/{id}/contact")
    public String contactDonation(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            requestService.contactDonationRegistration(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật trạng thái 'Đã liên hệ' thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/staff/donors/approved";
    }
    @GetMapping("/donors/contacted")
    public String showContactedDonorsPage(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DonationRegistration> contactedDonorsPage = requestService.findContactedDonationRegistrations(pageable);
        model.addAttribute("contactedDonorsPage", contactedDonorsPage);
        return "staff/contacted-donors";
    }

    // --- THÊM PHƯƠNG THỨC POST ĐỂ XỬ LÝ HÀNH ĐỘNG "HOÀN TẤT" ---
    @PostMapping("/donations/{id}/complete")
    public String completeDonation(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            requestService.completeDonation(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã hoàn tất quy trình hiến máu và cập nhật kho!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/staff/donors/contacted";
    }
}