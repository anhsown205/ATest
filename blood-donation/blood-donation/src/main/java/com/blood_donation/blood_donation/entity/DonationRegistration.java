package com.blood_donation.blood_donation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "donation_registrations")
public class DonationRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blood_type_id")
    private BloodType bloodType;

    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String province;
    private LocalDate availableDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private LocalDate nextEligibleDate;

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum Status {
        PENDING, CONTACTED, COMPLETED, CANCELLED, APPROVED, REJECTED
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
