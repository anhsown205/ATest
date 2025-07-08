package com.blood_donation.blood_donation.repository;

import com.blood_donation.blood_donation.entity.BloodUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface BloodUnitRepository extends JpaRepository<BloodUnit, Integer> {
    @Query("SELECT new com.blood_donation.blood_donation.dto.BloodUnitSummaryDto(bu.bloodType, SUM(bu.quantity)) " +
            "FROM BloodUnit bu WHERE bu.status = 'AVAILABLE' GROUP BY bu.bloodType")
    List<com.blood_donation.blood_donation.dto.BloodUnitSummaryDto> getInventorySummary();
     @Query("SELECT SUM(bu.quantity) FROM BloodUnit bu WHERE bu.bloodType.id = ?1 AND bu.status = 'AVAILABLE'")
    Long getInventorySummaryByBloodType(Integer bloodTypeId);

    List<BloodUnit> findByBloodTypeIdAndStatusOrderByExpiryDateAsc(Integer bloodTypeId, BloodUnit.Status status);
}