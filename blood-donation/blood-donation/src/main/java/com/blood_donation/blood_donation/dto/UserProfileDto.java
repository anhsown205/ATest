package com.blood_donation.blood_donation.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserProfileDto {
    private String fullName;
    private String email;
    private String nationalId;
    private LocalDate dateOfBirth; // <-- THÊM DÒNG NÀY
    private String phone;         // <-- THÊM DÒNG NÀY
    private String position;
}