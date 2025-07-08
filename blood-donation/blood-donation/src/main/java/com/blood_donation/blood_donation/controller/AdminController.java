package com.blood_donation.blood_donation.controller;

import com.blood_donation.blood_donation.entity.EmergencyRequest;
import com.blood_donation.blood_donation.entity.User;
import com.blood_donation.blood_donation.repository.BloodTypeRepository;
import com.blood_donation.blood_donation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.blood_donation.blood_donation.dto.AdminUserCreationDto; // Import DTO mới
import com.blood_donation.blood_donation.dto.BloodUnitDto;

import org.springframework.web.bind.annotation.ModelAttribute; // Thêm import
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; // Thêm import
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Thêm import

import com.blood_donation.blood_donation.service.BloodInventoryService;
import com.blood_donation.blood_donation.service.EmergencyRequestService; // Thêm import


@Controller
@RequestMapping("/admin") // Tất cả các request trong controller này sẽ bắt đầu bằng /admin
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmergencyRequestService emergencyRequestService;
    @Autowired
    private BloodInventoryService inventoryService;
    @Autowired
    private BloodTypeRepository bloodTypeRepository;
    @GetMapping("/inventory")
    public String showInventoryManagement(Model model) {
        model.addAttribute("inventorySummary", inventoryService.getInventorySummary());
        model.addAttribute("bloodTypes", bloodTypeRepository.findAll());
        model.addAttribute("newBloodUnit", new BloodUnitDto());
        return "admin/inventory-management";
    }

    @PostMapping("/inventory/add")
    public String addBloodUnit(@ModelAttribute("newBloodUnit") BloodUnitDto bloodUnitDto,
                               RedirectAttributes redirectAttributes) {
        try {
            inventoryService.addNewBloodUnit(bloodUnitDto);
            redirectAttributes.addFlashAttribute("successMessage", "Đã thêm đơn vị máu vào kho thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/inventory";
    }

    // Ánh xạ tới /admin/users
    @GetMapping("/users")
    public String listUsers(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userService.findAllUsers(pageable);

        model.addAttribute("userPage", userPage);

        return "admin/user-list"; // Trả về file user-list.html trong thư mục con admin
    }

    @GetMapping("/users/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("userDto", new AdminUserCreationDto());
        // Đưa danh sách các vai trò vào model để hiển thị trong dropdown
        model.addAttribute("roles", User.Role.values());
        return "admin/user-add";
    }
     @PostMapping("/users/save")
    public String saveNewUser(@ModelAttribute("userDto") AdminUserCreationDto userDto,
                              RedirectAttributes redirectAttributes) {
        try {
            userService.createUserByAdmin(userDto);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm người dùng mới thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            // Nếu lỗi, trả về lại form add và giữ lại dữ liệu đã nhập
            redirectAttributes.addFlashAttribute("userDto", userDto);
            return "redirect:/admin/users/add";
        }
        return "redirect:/admin/users";
    }
     @GetMapping("/emergency-requests")
    public String showEmergencyRequestList(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EmergencyRequest> requestPage = emergencyRequestService.findAllRequests(pageable);
        model.addAttribute("requestPage", requestPage);
        return "admin/emergency-request-list";
    }

    @PostMapping("/emergency-requests/{id}/approve")
    public String approveEmergencyRequest(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            emergencyRequestService.approveRequest(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã phê duyệt yêu cầu thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/emergency-requests";
    }

    @PostMapping("/emergency-requests/{id}/reject")
    public String rejectEmergencyRequest(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            emergencyRequestService.rejectRequest(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã từ chối yêu cầu.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/emergency-requests";
    }
}