package com.blood_donation.blood_donation.service;

import com.blood_donation.blood_donation.dto.UserRegistrationDto;
import com.blood_donation.blood_donation.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.blood_donation.blood_donation.dto.AdminUserCreationDto; // Import DTO mới
import com.blood_donation.blood_donation.dto.AdminUserEditDto;
import com.blood_donation.blood_donation.dto.UserProfileDto;
import java.util.Optional; // Thêm import
import com.blood_donation.blood_donation.dto.PasswordChangeDto; // Thêm import


public interface UserService {
    User registerNewUser(UserRegistrationDto registrationDto);
    Page<User> findAllUsers(Pageable pageable);
    User createUserByAdmin(AdminUserCreationDto creationDto);
    Optional<User> findByUsername(String username);
    void updateUserProfile(String username, UserProfileDto profileDto);

    void resetPasswordToDefault(String userEmail);
    void changeUserPassword(String username, PasswordChangeDto passwordChangeDto);
    void lockUser(Integer userIdToLock, String adminUsername);
    void unlockUser(Integer userId);

    void updateUserByAdmin(AdminUserEditDto userDto);

    Optional<User> findById(Integer id);

    void deleteUser(Integer userIdToDelete, String adminUsername);

    Page<User> findAllUsers(Pageable pageable, String role);

}