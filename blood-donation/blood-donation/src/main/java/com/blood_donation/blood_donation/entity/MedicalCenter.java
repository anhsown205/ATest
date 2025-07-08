package com.blood_donation.blood_donation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medical_centers")
public class MedicalCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;

    @Column(columnDefinition = "TEXT")
    private String description;

    // --- RELATIONSHIPS ---
    @OneToMany(mappedBy = "medicalCenter", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BloodUnit> bloodUnits;
    // --- END OF RELATIONSHIPS ---
}