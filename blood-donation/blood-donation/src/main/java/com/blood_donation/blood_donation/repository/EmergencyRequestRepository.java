package com.blood_donation.blood_donation.repository;

import com.blood_donation.blood_donation.entity.EmergencyRequest;
import com.blood_donation.blood_donation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface EmergencyRequestRepository extends JpaRepository<EmergencyRequest, Integer> {

    // Lấy yêu cầu theo người tạo
    List<EmergencyRequest> findByRequesterUserOrderByCreatedAtDesc(User requesterUser);

    // Lấy yêu cầu theo trạng thái
    List<EmergencyRequest> findByStatusOrderByCreatedAtDesc(EmergencyRequest.Status status);

    // Đếm số yêu cầu theo trạng thái (thống kê)
    long countByStatus(EmergencyRequest.Status status);

    // Lấy 5 yêu cầu gần nhất theo trạng thái
    List<EmergencyRequest> findTop5ByStatusOrderByCreatedAtDesc(EmergencyRequest.Status status);

    // Phân trang danh sách yêu cầu theo trạng thái
    Page<EmergencyRequest> findByStatusOrderByCreatedAtDesc(EmergencyRequest.Status status, Pageable pageable);
    Page<EmergencyRequest> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
