package com.blood_donation.blood_donation.controller;

import com.blood_donation.blood_donation.service.BloodInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BloodInfoController {

    @Autowired
    private BloodInfoService bloodInfoService;

    @GetMapping("/blood-info")
    public String showBloodInfoPage(Model model) {
        model.addAttribute("bloodTypes", bloodInfoService.findAllBloodTypes());
        model.addAttribute("compatibilityData", bloodInfoService.getCompatibilityData());
        return "blood-info"; // Tên file view
    }
}