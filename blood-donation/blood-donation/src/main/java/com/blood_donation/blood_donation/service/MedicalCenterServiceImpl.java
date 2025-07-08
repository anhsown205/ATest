package com.blood_donation.blood_donation.service;

import com.blood_donation.blood_donation.entity.MedicalCenter;
import com.blood_donation.blood_donation.repository.MedicalCenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicalCenterServiceImpl implements MedicalCenterService {

    @Autowired
    private MedicalCenterRepository medicalCenterRepository;

    @Override
    public MedicalCenter findPrimaryCenter() {
        // Logic để tìm trung tâm chính.
        // Hiện tại, chúng ta tạm quy ước trung tâm chính có ID là 1.
        // .orElse(null) sẽ trả về null nếu không tìm thấy, tránh gây lỗi.
        return medicalCenterRepository.findById(1).orElse(null);
    }
}