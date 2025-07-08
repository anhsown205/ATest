package com.blood_donation.blood_donation.dto;

import com.blood_donation.blood_donation.entity.User;
import lombok.Data;

@Data
public class AdminUserEditDto {
    private Integer id;
    private String fullName;
    private String email;
    private User.Role role;
    private String username;
}