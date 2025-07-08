package com.blood_donation.blood_donation.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blood_donation.blood_donation.dto.DonationEditDto;
import com.blood_donation.blood_donation.dto.DonationRegistrationDto;
import com.blood_donation.blood_donation.entity.BloodType;
import com.blood_donation.blood_donation.entity.DonationRegistration;
import com.blood_donation.blood_donation.entity.User;
import com.blood_donation.blood_donation.repository.BloodTypeRepository;
import com.blood_donation.blood_donation.repository.DonationRegistrationRepository;
import com.blood_donation.blood_donation.repository.UserRepository;

@Service
public class DonationServiceImpl implements DonationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BloodTypeRepository bloodTypeRepository;

    @Autowired
    private DonationRegistrationRepository donationRegistrationRepository;

    @Override
    public void createDonationRegistration(DonationRegistrationDto dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng: " + username));

        // 1. Kiểm tra không có đăng ký nào đang hoạt động
        List<DonationRegistration.Status> activeStatuses = List.of(
            DonationRegistration.Status.PENDING,
            DonationRegistration.Status.APPROVED,
            DonationRegistration.Status.CONTACTED
        );
        if (donationRegistrationRepository.existsByUserAndStatusIn(user, activeStatuses)) {
            throw new RuntimeException("Bạn đã có một đăng ký hiến máu đang hoạt động. Vui lòng hoàn tất hoặc hủy đăng ký cũ.");
        }

        // 2. Kiểm tra ngày được chọn không được là quá khứ
        if (dto.getAvailableDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Ngày bạn chọn để hiến máu không thể là một ngày trong quá khứ.");
        }

        // 3. Kiểm tra ngày hợp lệ dựa trên lần hiến máu gần nhất
        Optional<DonationRegistration> lastCompletedDonation = donationRegistrationRepository
            .findTopByUserAndStatusOrderByCompletedAtDesc(user, DonationRegistration.Status.COMPLETED);

        if (lastCompletedDonation.isPresent()) {
            LocalDate nextEligibleDate = lastCompletedDonation.get().getNextEligibleDate();
            if (dto.getAvailableDate().isBefore(nextEligibleDate)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                throw new RuntimeException("Bạn chưa đủ thời gian phục hồi. Bạn chỉ có thể hiến máu lại sau ngày " + nextEligibleDate.format(formatter) + ".");
            }
        }

        // 4. Tạo bản ghi mới với trạng thái PENDING cho tất cả mọi người
        BloodType bloodType = bloodTypeRepository.findById(dto.getBloodTypeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm máu với ID: " + dto.getBloodTypeId()));

        DonationRegistration registration = new DonationRegistration();
        registration.setUser(user);
        registration.setBloodType(bloodType);
        registration.setAddress(dto.getAddress());
        registration.setPhone(dto.getPhone());
        registration.setGender(dto.getGender());
        registration.setProvince(dto.getProvince());
        registration.setAvailableDate(dto.getAvailableDate());
        registration.setStatus(DonationRegistration.Status.PENDING);

        donationRegistrationRepository.save(registration);
    }

    @Override
    public List<DonationRegistration> findDonationHistoryByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng: " + username));
        return donationRegistrationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public Optional<DonationRegistration> findRegistrationByIdAndUsername(Integer id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng: " + username));
        return donationRegistrationRepository.findById(id)
                .filter(registration -> registration.getUser().equals(user));
    }

    @Override
    public void updateDonationRegistration(DonationEditDto dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng: " + username));

        DonationRegistration registration = findRegistrationByIdAndUsername(dto.getId(), username)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy hoặc không có quyền chỉnh sửa đăng ký này."));

        if (registration.getStatus() != DonationRegistration.Status.PENDING && registration.getStatus() != DonationRegistration.Status.APPROVED) {
            throw new IllegalStateException("Bạn không thể chỉnh sửa một đăng ký đã được xử lý.");
        }

        if (dto.getAvailableDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Ngày hẹn hiến không thể là một ngày trong quá khứ.");
        }

        Optional<DonationRegistration> lastCompletedDonation = donationRegistrationRepository
            .findTopByUserAndStatusOrderByCompletedAtDesc(user, DonationRegistration.Status.COMPLETED);

        if (lastCompletedDonation.isPresent()) {
            LocalDate nextEligibleDate = lastCompletedDonation.get().getNextEligibleDate();
            if (dto.getAvailableDate().isBefore(nextEligibleDate)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                throw new RuntimeException("Bạn chưa đủ thời gian phục hồi. Bạn chỉ có thể hiến máu lại sau ngày " + nextEligibleDate.format(formatter) + ".");
            }
        }

        registration.setPhone(dto.getPhone());
        registration.setAddress(dto.getAddress());
        registration.setProvince(dto.getProvince());
        registration.setAvailableDate(dto.getAvailableDate());
        registration.setStatus(DonationRegistration.Status.PENDING); // Reset lại trạng thái để nhân viên duyệt lại

        donationRegistrationRepository.save(registration);
    }
}