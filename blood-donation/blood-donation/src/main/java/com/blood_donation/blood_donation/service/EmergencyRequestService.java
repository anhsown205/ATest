package com.blood_donation.blood_donation.service;

import com.blood_donation.blood_donation.dto.EmergencyRequestDto;
import com.blood_donation.blood_donation.entity.EmergencyRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmergencyRequestService {
    Page<EmergencyRequest> findAllRequests(Pageable pageable);
    void approveRequest(Integer requestId);
    void rejectRequest(Integer requestId);
    void createEmergencyRequest(EmergencyRequestDto dto, String username);

}