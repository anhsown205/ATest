package com.blood_donation.blood_donation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "blood_types")
public class BloodType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String bloodGroup;

    @Enumerated(EnumType.STRING)
    private RhFactor rhFactor;

    // --- RELATIONSHIPS ---
    @OneToMany(mappedBy = "bloodType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DonationRegistration> donationRegistrations;

    @OneToMany(mappedBy = "bloodType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<EmergencyRequest> emergencyRequests;

    @OneToMany(mappedBy = "bloodType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BloodUnit> bloodUnits;

    @OneToMany(mappedBy = "donorBloodType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BloodCompatibilityRule> donorRules;

    @OneToMany(mappedBy = "recipientBloodType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BloodCompatibilityRule> recipientRules;
    // --- END OF RELATIONSHIPS ---

    public enum RhFactor {
        POSITIVE, NEGATIVE
    }

    @Override
    public String toString() {
        // Trả về chuỗi hiển thị đẹp mắt như "A+", "O-", ...
        // thay vì chuỗi debug mặc định.
        return this.bloodGroup + (this.rhFactor == RhFactor.POSITIVE ? "+" : "-");
    }
}