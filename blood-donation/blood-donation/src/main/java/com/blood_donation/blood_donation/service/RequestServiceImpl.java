package com.blood_donation.blood_donation.service;

import com.blood_donation.blood_donation.entity.BloodUnit;
import com.blood_donation.blood_donation.entity.DonationRegistration;
import com.blood_donation.blood_donation.entity.EmergencyRequest;
import com.blood_donation.blood_donation.entity.MedicalCenter;
import com.blood_donation.blood_donation.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private EmergencyRequestRepository emergencyRequestRepository;
    @Autowired
    private DonationRegistrationRepository donationRegistrationRepository;
    @Autowired
    private BloodUnitRepository bloodUnitRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BloodTypeRepository bloodTypeRepository;
    @Autowired
    private MedicalCenterRepository medicalCenterRepository;

    @Override
    public Page<EmergencyRequest> findPendingEmergencyRequests(Pageable pageable) {
        return emergencyRequestRepository.findByStatusOrderByCreatedAtDesc(EmergencyRequest.Status.PENDING, pageable);
    }

    @Override
    public Page<DonationRegistration> findPendingDonationRegistrations(Pageable pageable) {
        return donationRegistrationRepository.findByStatusOrderByCreatedAtDesc(DonationRegistration.Status.PENDING, pageable);
    }

    @Override
    @Transactional
    public void approveDonationRegistration(Integer registrationId) {
        DonationRegistration registration = donationRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đăng ký với ID: " + registrationId));

        if (registration.getStatus() != DonationRegistration.Status.PENDING) {
            throw new IllegalStateException("Chỉ có thể chấp thuận đăng ký đang ở trạng thái chờ.");
        }

        registration.setStatus(DonationRegistration.Status.APPROVED);
        donationRegistrationRepository.save(registration);
    }

    @Override
    @Transactional
    public void rejectDonationRegistration(Integer registrationId) {
        DonationRegistration registration = donationRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đăng ký với ID: " + registrationId));

        if (registration.getStatus() != DonationRegistration.Status.PENDING) {
            throw new IllegalStateException("Chỉ có thể từ chối đăng ký đang ở trạng thái chờ.");
        }

        registration.setStatus(DonationRegistration.Status.REJECTED);
        donationRegistrationRepository.save(registration);
    }

    @Override
    public Page<DonationRegistration> findApprovedDonationRegistrations(Pageable pageable) {
        return donationRegistrationRepository.findByStatusOrderByCreatedAtDesc(DonationRegistration.Status.APPROVED, pageable);
    }

    @Override
    @Transactional
    public void contactDonationRegistration(Integer registrationId) {
        DonationRegistration registration = donationRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đăng ký với ID: " + registrationId));

        if (registration.getStatus() != DonationRegistration.Status.APPROVED) {
            throw new IllegalStateException("Chỉ có thể liên hệ với người hiến máu đã được chấp thuận.");
        }

        registration.setStatus(DonationRegistration.Status.CONTACTED);
        donationRegistrationRepository.save(registration);
    }

    @Override
    public Page<DonationRegistration> findContactedDonationRegistrations(Pageable pageable) {
        return donationRegistrationRepository.findByStatusOrderByCreatedAtDesc(DonationRegistration.Status.CONTACTED, pageable);
    }

    @Override
    @Transactional
    public void completeDonation(Integer registrationId) {
        DonationRegistration registration = donationRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đăng ký với ID: " + registrationId));

        if (registration.getStatus() != DonationRegistration.Status.CONTACTED) {
            throw new IllegalStateException("Chỉ có thể hoàn tất lượt hiến máu đã được liên hệ.");
        }

        registration.setStatus(DonationRegistration.Status.COMPLETED);
        registration.setCompletedAt(LocalDateTime.now());
        registration.setNextEligibleDate(LocalDate.now().plusDays(84));
        donationRegistrationRepository.save(registration);

        MedicalCenter defaultCenter = medicalCenterRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trung tâm y tế mặc định với ID = 1. Vui lòng kiểm tra CSDL."));

        BloodUnit newBloodUnit = new BloodUnit();
        newBloodUnit.setBloodType(registration.getBloodType());
        newBloodUnit.setMedicalCenter(defaultCenter);
        newBloodUnit.setQuantity(1);
        newBloodUnit.setStatus(BloodUnit.Status.AVAILABLE);
        newBloodUnit.setExpiryDate(LocalDate.now().plusDays(42));
        bloodUnitRepository.save(newBloodUnit);
    }
}