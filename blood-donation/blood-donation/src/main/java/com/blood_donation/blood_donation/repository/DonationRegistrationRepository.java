package com.blood_donation.blood_donation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blood_donation.blood_donation.entity.DonationRegistration;
import com.blood_donation.blood_donation.entity.User;

@Repository
public interface DonationRegistrationRepository extends JpaRepository<DonationRegistration, Integer> {

    List<DonationRegistration> findByUserOrderByCreatedAtDesc(User user);

    boolean existsByUserAndStatus(User user, DonationRegistration.Status status);

    Page<DonationRegistration> findByStatusOrderByCreatedAtDesc(DonationRegistration.Status status, Pageable pageable);

    Optional<DonationRegistration> findTopByUserAndStatusOrderByCompletedAtDesc(User user, DonationRegistration.Status status);

    boolean existsByUserAndStatusIn(User user, List<DonationRegistration.Status> statuses);

    List<DonationRegistration> findTop5ByStatusOrderByCreatedAtDesc(DonationRegistration.Status status);

    long countByStatus(DonationRegistration.Status status);

}