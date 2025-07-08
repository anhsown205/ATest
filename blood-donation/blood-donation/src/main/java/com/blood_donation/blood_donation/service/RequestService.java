package com.blood_donation.blood_donation.service;

import com.blood_donation.blood_donation.entity.DonationRegistration;
import com.blood_donation.blood_donation.entity.EmergencyRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RequestService {
    Page<EmergencyRequest> findPendingEmergencyRequests(Pageable pageable);
    Page<DonationRegistration> findPendingDonationRegistrations(Pageable pageable);
    void approveDonationRegistration(Integer registrationId);
    void rejectDonationRegistration(Integer registrationId);
    Page<DonationRegistration> findApprovedDonationRegistrations(Pageable pageable);
    void contactDonationRegistration(Integer registrationId);
    Page<DonationRegistration> findContactedDonationRegistrations(Pageable pageable);
    void completeDonation(Integer registrationId);
}