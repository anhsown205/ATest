package com.blood_donation.blood_donation.dto;

import com.blood_donation.blood_donation.entity.BloodType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // Cần constructor này để query của JPA hoạt động
public class BloodUnitSummaryDto {
    private BloodType bloodType;
    private Long totalQuantity;
}