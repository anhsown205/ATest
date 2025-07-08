package com.blood_donation.blood_donation.service;

import com.blood_donation.blood_donation.dto.CompatibilityDataDto;
import com.blood_donation.blood_donation.entity.BloodCompatibilityRule;
import com.blood_donation.blood_donation.entity.BloodType;
import com.blood_donation.blood_donation.repository.BloodCompatibilityRuleRepository;
import com.blood_donation.blood_donation.repository.BloodTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class BloodInfoServiceImpl implements BloodInfoService {

    @Autowired
    private BloodTypeRepository bloodTypeRepository;
    @Autowired
    private BloodCompatibilityRuleRepository ruleRepository;

    @Override
    public List<BloodType> findAllBloodTypes() {
        return bloodTypeRepository.findAll();
    }

    @Override
    public Map<BloodType, CompatibilityDataDto> getCompatibilityData() {
        Map<BloodType, CompatibilityDataDto> compatibilityMap = new LinkedHashMap<>();
        List<BloodType> allBloodTypes = bloodTypeRepository.findAll();
        List<BloodCompatibilityRule> allRules = ruleRepository.findAll();

        // Khởi tạo map với tất cả các nhóm máu là người nhận
        for (BloodType recipient : allBloodTypes) {
            compatibilityMap.put(recipient, new CompatibilityDataDto());
        }

        // Lặp qua các quy tắc và điền vào map
        for (BloodCompatibilityRule rule : allRules) {
            BloodType recipient = rule.getRecipientBloodType();
            BloodType donor = rule.getDonorBloodType();
            CompatibilityDataDto dto = compatibilityMap.get(recipient);

            if (dto != null) {
                switch (rule.getComponent()) {
                    case RED_CELLS:
                        dto.getCompatibleRedCellDonors().add(donor);
                        break;
                    case PLASMA:
                        dto.getCompatiblePlasmaDonors().add(donor);
                        break;
                    case WHOLE_BLOOD:
                        dto.getCompatibleWholeBloodDonors().add(donor);
                        break;
                    case PLATELETS:
                        dto.getCompatiblePlateletDonors().add(donor);
                        break;
                    // Thêm các case khác nếu cần
                }
            }
        }
        return compatibilityMap;
    }
}