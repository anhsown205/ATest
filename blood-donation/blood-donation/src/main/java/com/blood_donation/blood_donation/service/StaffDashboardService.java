package com.blood_donation.blood_donation.service;

import com.blood_donation.blood_donation.dto.BloodUnitSummaryDto;
import com.blood_donation.blood_donation.entity.EmergencyRequest;

import java.util.List;

public interface StaffDashboardService {
    long getPendingEmergencyRequestCount();
    long getPendingDonationRegistrationCount();
    List<BloodUnitSummaryDto> getInventorySummary();
    List<EmergencyRequest> getRecentPendingEmergencyRequests(int limit);
}