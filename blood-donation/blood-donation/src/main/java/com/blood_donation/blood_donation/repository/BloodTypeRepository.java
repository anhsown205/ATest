package com.blood_donation.blood_donation.repository;

import com.blood_donation.blood_donation.entity.BloodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodTypeRepository extends JpaRepository<BloodType, Integer> {
    // Các phương thức CRUD cơ bản được cung cấp sẵn là đủ cho nhu cầu hiện tại
}