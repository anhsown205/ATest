package com.blood_donation.blood_donation.service;

import com.blood_donation.blood_donation.dto.BloodUnitDto;
import com.blood_donation.blood_donation.dto.BloodUnitSummaryDto;
import com.blood_donation.blood_donation.entity.BloodType;
import com.blood_donation.blood_donation.entity.BloodUnit;
import com.blood_donation.blood_donation.entity.MedicalCenter;
import com.blood_donation.blood_donation.repository.BloodTypeRepository;
import com.blood_donation.blood_donation.repository.BloodUnitRepository;
import com.blood_donation.blood_donation.repository.MedicalCenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BloodInventoryServiceImpl implements BloodInventoryService {
    @Autowired private BloodUnitRepository bloodUnitRepository;
    @Autowired private BloodTypeRepository bloodTypeRepository;
    @Autowired private MedicalCenterRepository medicalCenterRepository;

    @Override
    public List<BloodUnitSummaryDto> getInventorySummary() {
        return bloodUnitRepository.getInventorySummary();
    }

    @Override
    public void addNewBloodUnit(BloodUnitDto dto) {
        BloodType bloodType = bloodTypeRepository.findById(dto.getBloodTypeId())
                .orElseThrow(() -> new RuntimeException("Nhóm máu không hợp lệ."));
        // Giả định luôn thêm vào trung tâm y tế có ID=1
        MedicalCenter center = medicalCenterRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trung tâm y tế mặc định."));

        BloodUnit newUnit = new BloodUnit();
        newUnit.setBloodType(bloodType);
        newUnit.setMedicalCenter(center);
        newUnit.setQuantity(dto.getQuantity());
        newUnit.setExpiryDate(dto.getExpiryDate());
        newUnit.setStatus(BloodUnit.Status.AVAILABLE);

        bloodUnitRepository.save(newUnit);
    }
}
