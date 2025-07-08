package com.blood_donation.blood_donation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "blood_units")
public class BloodUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blood_type_id")
    private BloodType bloodType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_center_id")
    private MedicalCenter medicalCenter;

    private LocalDate expiryDate;
    private LocalDateTime receivedAt;

    public enum Status {
        AVAILABLE, RESERVED, USED
    }

    @PrePersist
    protected void onReceive() {
        receivedAt = LocalDateTime.now();
    }
}