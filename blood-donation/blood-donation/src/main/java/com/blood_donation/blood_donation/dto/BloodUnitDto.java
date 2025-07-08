package com.blood_donation.blood_donation.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class BloodUnitDto {
    private Integer bloodTypeId;
    private Integer quantity;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;
}