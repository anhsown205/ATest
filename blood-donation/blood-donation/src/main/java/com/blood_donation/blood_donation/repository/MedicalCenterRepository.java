package com.blood_donation.blood_donation.repository;

import com.blood_donation.blood_donation.entity.MedicalCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalCenterRepository extends JpaRepository<MedicalCenter, Integer> {
}