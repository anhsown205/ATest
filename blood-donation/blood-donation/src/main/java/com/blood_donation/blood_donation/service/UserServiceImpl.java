package com.blood_donation.blood_donation.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blood_donation.blood_donation.dto.AdminUserCreationDto;
import com.blood_donation.blood_donation.dto.UserProfileDto;
import com.blood_donation.blood_donation.dto.UserRegistrationDto;
import com.blood_donation.blood_donation.entity.User;
import com.blood_donation.blood_donation.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerNewUser(UserRegistrationDto registrationDto) {
        if (registrationDto.getDateOfBirth() == null) {
            throw new RuntimeException("Vui lòng nhập ngày sinh của bạn.");
        }
        int age = Period.between(registrationDto.getDateOfBirth(), LocalDate.now()).getYears();
        if (age < 18) {
            throw new RuntimeException("Bạn chưa đủ 18 tuổi để đăng ký tài khoản và hiến máu.");
        }
        // Kiểm tra mật khẩu có khớp không
        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp!");
        }
        // Kiểm tra username đã tồn tại chưa
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        // Tạo đối tượng User từ DTO
        User user = new User();
        user.setFullName(registrationDto.getFullName());
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        // Mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        // Gán vai trò mặc định
        user.setRole(User.Role.MEMBER);

        return userRepository.save(user);
    }

    @Override
    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User createUserByAdmin(AdminUserCreationDto creationDto) {
        if (userRepository.existsByUsername(creationDto.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }
        if (userRepository.existsByEmail(creationDto.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        User user = new User();
        user.setFullName(creationDto.getFullName());
        user.setUsername(creationDto.getUsername());
        user.setEmail(creationDto.getEmail());
        user.setPassword(passwordEncoder.encode(creationDto.getPassword()));

        // Gán vai trò được chọn bởi Admin
        user.setRole(creationDto.getRole());

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional // Đảm bảo tất cả các thay đổi được lưu hoặc không lưu gì cả
    public void updateUserProfile(String username, UserProfileDto profileDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        // Kiểm tra xem email mới có bị trùng với người dùng khác không
        Optional<User> userWithNewEmail = userRepository.findByEmail(profileDto.getEmail());
        if (userWithNewEmail.isPresent() && !userWithNewEmail.get().getId().equals(user.getId())) {
            throw new RuntimeException("Email đã được sử dụng bởi một tài khoản khác.");
        }

        // Cập nhật thông tin
        user.setFullName(profileDto.getFullName());
        user.setEmail(profileDto.getEmail());
        user.setNationalId(profileDto.getNationalId());

        userRepository.save(user);
    }
}