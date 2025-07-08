package com.blood_donation.blood_donation.service;

import com.blood_donation.blood_donation.entity.MedicalCenter;

public interface MedicalCenterService {
    /**
     * Tìm thông tin của trung tâm y tế chính để hiển thị trên trang chủ.
     * @return Đối tượng MedicalCenter hoặc null nếu không tìm thấy.
     */
    MedicalCenter findPrimaryCenter();
}