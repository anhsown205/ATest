package com.blood_donation.blood_donation.dto;

import lombok.Data;

@Data
public class UserProfileDto {
    private String fullName;
    private String email;
    private String nationalId;
}