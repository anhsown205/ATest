package com.blood_donation.blood_donation.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;


@Data
public class UserRegistrationDto {
    private String fullName;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    @DateTimeFormat(pattern = "yyyy-MM-dd") 
    private LocalDate dateOfBirth;
    private String phone;
}