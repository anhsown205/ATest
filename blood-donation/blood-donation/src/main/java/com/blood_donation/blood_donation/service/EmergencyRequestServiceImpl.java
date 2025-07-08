package com.blood_donation.blood_donation.service;

import com.blood_donation.blood_donation.dto.EmergencyRequestDto;
import com.blood_donation.blood_donation.entity.BloodType;
import com.blood_donation.blood_donation.entity.BloodUnit;
import com.blood_donation.blood_donation.entity.EmergencyRequest;
import com.blood_donation.blood_donation.repository.BloodTypeRepository;
import com.blood_donation.blood_donation.repository.BloodUnitRepository;
import com.blood_donation.blood_donation.repository.EmergencyRequestRepository;
import com.blood_donation.blood_donation.repository.UserRepository;
import com.blood_donation.blood_donation.entity.User;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmergencyRequestServiceImpl implements EmergencyRequestService {

    @Autowired
    private EmergencyRequestRepository emergencyRequestRepository;

    @Autowired
    private BloodUnitRepository bloodUnitRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BloodTypeRepository bloodTypeRepository;

    @Override
    public Page<EmergencyRequest> findAllRequests(Pageable pageable) {
        return emergencyRequestRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    @Transactional
    public void approveRequest(Integer requestId) {
        EmergencyRequest request = emergencyRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu với ID: " + requestId));

        if (request.getStatus() != EmergencyRequest.Status.PENDING) {
            throw new IllegalStateException("Chỉ có thể phê duyệt yêu cầu đang ở trạng thái chờ.");
        }

        // Lấy tổng số lượng máu có sẵn trong kho cho nhóm máu này
        Long availableQuantity = bloodUnitRepository.getInventorySummaryByBloodType(request.getBloodType().getId());
        if (availableQuantity == null) availableQuantity = 0L;

        // Kịch bản 1: Kho đủ máu
        if (availableQuantity >= request.getQuantityNeeded()) {
            // Trừ máu từ kho
            int quantityToDeduct = request.getQuantityNeeded();
            List<BloodUnit> availableUnits = bloodUnitRepository.findByBloodTypeIdAndStatusOrderByExpiryDateAsc(request.getBloodType().getId(), BloodUnit.Status.AVAILABLE);

            for (BloodUnit unit : availableUnits) {
                if (quantityToDeduct <= 0) break;
                int deductAmount = Math.min(quantityToDeduct, unit.getQuantity());
                unit.setQuantity(unit.getQuantity() - deductAmount);
                if (unit.getQuantity() == 0) {
                    unit.setStatus(BloodUnit.Status.USED);
                }
                bloodUnitRepository.save(unit);
                quantityToDeduct -= deductAmount;
            }
            // Đánh dấu yêu cầu là hoàn thành
            request.setStatus(EmergencyRequest.Status.COMPLETED);
        } else {
            // Kịch bản 2: Kho không đủ máu
            request.setStatus(EmergencyRequest.Status.PROCESSING);
        }
        emergencyRequestRepository.save(request);
    }

    @Override
    @Transactional
    public void rejectRequest(Integer requestId) {
        EmergencyRequest request = emergencyRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu với ID: " + requestId));

        if (request.getStatus() != EmergencyRequest.Status.PENDING) {
            throw new IllegalStateException("Chỉ có thể từ chối yêu cầu đang ở trạng thái chờ.");
        }
        request.setStatus(EmergencyRequest.Status.CANCELLED);
        emergencyRequestRepository.save(request);
    }
    @Override
    public void createEmergencyRequest(EmergencyRequestDto dto, String username) {
        User requester = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng."));
        BloodType bloodType = bloodTypeRepository.findById(dto.getBloodTypeId())
                .orElseThrow(() -> new RuntimeException("Nhóm máu không hợp lệ."));

        EmergencyRequest newRequest = new EmergencyRequest();
        newRequest.setRequesterUser(requester);
        newRequest.setPatientName(dto.getPatientName());
        newRequest.setBloodType(bloodType);
        newRequest.setQuantityNeeded(dto.getQuantityNeeded());
        newRequest.setAddress(dto.getAddress());
        newRequest.setPhone(dto.getPhone());
        newRequest.setReason(dto.getReason());
        newRequest.setStatus(EmergencyRequest.Status.PENDING);

        emergencyRequestRepository.save(newRequest);
    }
}