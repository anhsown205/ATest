package com.blood_donation.blood_donation.dto;

import lombok.Data;

@Data
public class EmergencyRequestDto {
    private String patientName;
    private Integer bloodTypeId;
    private int quantityNeeded;
    private String address;
    private String phone;
    private String reason;
}
