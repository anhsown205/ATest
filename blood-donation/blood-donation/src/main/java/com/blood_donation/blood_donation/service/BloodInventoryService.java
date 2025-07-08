package com.blood_donation.blood_donation.service;

import com.blood_donation.blood_donation.dto.BloodUnitDto;
import com.blood_donation.blood_donation.dto.BloodUnitSummaryDto;
import java.util.List;

public interface BloodInventoryService {
    List<BloodUnitSummaryDto> getInventorySummary();
    void addNewBloodUnit(BloodUnitDto bloodUnitDto);
}