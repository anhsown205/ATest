package com.blood_donation.blood_donation.dto;

import com.blood_donation.blood_donation.entity.User;
import lombok.Data;

@Data
public class AdminUserCreationDto {
    private String fullName;
    private String username;
    private String email;
    private String password;
    private User.Role role; // Thêm trường role để Admin có thể chọn
}