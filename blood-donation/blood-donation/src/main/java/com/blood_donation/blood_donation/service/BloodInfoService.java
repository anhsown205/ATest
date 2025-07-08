package com.blood_donation.blood_donation.service;

import com.blood_donation.blood_donation.dto.CompatibilityDataDto;
import com.blood_donation.blood_donation.entity.BloodType;
import java.util.List;
import java.util.Map;

public interface BloodInfoService {
    List<BloodType> findAllBloodTypes();
    Map<BloodType, CompatibilityDataDto> getCompatibilityData();
}