package com.blood_donation.blood_donation.service;

import com.blood_donation.blood_donation.dto.BloodUnitSummaryDto;
import com.blood_donation.blood_donation.entity.DonationRegistration;
import com.blood_donation.blood_donation.entity.EmergencyRequest;
import com.blood_donation.blood_donation.repository.BloodUnitRepository;
import com.blood_donation.blood_donation.repository.DonationRegistrationRepository;
import com.blood_donation.blood_donation.repository.EmergencyRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffDashboardServiceImpl implements StaffDashboardService {

    @Autowired private EmergencyRequestRepository emergencyRequestRepository;
    @Autowired private DonationRegistrationRepository donationRegistrationRepository;
    @Autowired private BloodUnitRepository bloodUnitRepository;

    @Override
    public long getPendingEmergencyRequestCount() {
        return emergencyRequestRepository.countByStatus(EmergencyRequest.Status.PENDING);
    }

    @Override
    public long getPendingDonationRegistrationCount() {
        return donationRegistrationRepository.countByStatus(DonationRegistration.Status.PENDING);
    }

    @Override
    public List<BloodUnitSummaryDto> getInventorySummary() {
        return bloodUnitRepository.getInventorySummary();
    }

    @Override
    public List<EmergencyRequest> getRecentPendingEmergencyRequests(int limit) {
        // Lưu ý: limit không được sử dụng ở đây vì ta đã dùng findTop5
        return emergencyRequestRepository.findTop5ByStatusOrderByCreatedAtDesc(EmergencyRequest.Status.PENDING);
    }
}