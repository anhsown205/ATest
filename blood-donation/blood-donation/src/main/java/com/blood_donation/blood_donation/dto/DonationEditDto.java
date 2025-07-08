package com.blood_donation.blood_donation.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class DonationEditDto {
    private Integer id;
    private String phone;
    private String address;
    private String province;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate availableDate;
}