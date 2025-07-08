package com.blood_donation.blood_donation.dto;

import com.blood_donation.blood_donation.entity.DonationRegistration;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class DonationRegistrationDto {
    private Integer bloodTypeId;
    private String address;
    private String phone;
    private DonationRegistration.Gender gender;
    private String province;

    // Giúp Spring Boot hiểu đúng định dạng ngày tháng từ form HTML (ví dụ: yyyy-MM-dd)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate availableDate;

}
