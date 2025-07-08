package com.blood_donation.blood_donation.dto;

import com.blood_donation.blood_donation.entity.BloodType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CompatibilityDataDto {
    // Chúng ta có thể thêm các list khác cho WHOLE_BLOOD, PLATELETS nếu cần
    private List<BloodType> compatibleRedCellDonors = new ArrayList<>();
    private List<BloodType> compatiblePlasmaDonors = new ArrayList<>();
    private List<BloodType> compatibleWholeBloodDonors = new ArrayList<>();
    private List<BloodType> compatiblePlateletDonors = new ArrayList<>();
}