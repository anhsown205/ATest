package com.blood_donation.blood_donation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "blood_compatibility_rules")
public class BloodCompatibilityRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_blood_type_id")
    private BloodType donorBloodType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_blood_type_id")
    private BloodType recipientBloodType;

    @Enumerated(EnumType.STRING)
    private Component component;

    public enum Component {
        WHOLE_BLOOD, RED_CELLS, PLASMA, PLATELETS
    }
}