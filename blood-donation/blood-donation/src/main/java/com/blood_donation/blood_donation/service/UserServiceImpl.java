package com.blood_donation.blood_donation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blood_donation.blood_donation.dto.AdminUserCreationDto;
import com.blood_donation.blood_donation.dto.AdminUserEditDto;
import com.blood_donation.blood_donation.dto.UserProfileDto;
import com.blood_donation.blood_donation.dto.UserRegistrationDto;
import com.blood_donation.blood_donation.entity.User;
import com.blood_donation.blood_donation.repository.UserRepository;

import jakarta.transaction.Transactional;
import com.blood_donation.blood_donation.dto.PasswordChangeDto; // Thêm import
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ...
    @Autowired
    private SessionRegistry sessionRegistry; // Inject SessionRegistry

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
        user.setDateOfBirth(registrationDto.getDateOfBirth()); // <-- THÊM DÒNG NÀY
        user.setPhone(registrationDto.getPhone());  
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
        user.setDateOfBirth(profileDto.getDateOfBirth()); // <-- THÊM DÒNG NÀY
        user.setPhone(profileDto.getPhone());
        // Chỉ cập nhật chức vụ nếu người dùng là Staff hoặc Admin
        if(user.getRole() == User.Role.STAFF || user.getRole() == User.Role.ADMIN) {
            user.setPosition(profileDto.getPosition()); // <-- THÊM DÒNG NÀY
        }  

        userRepository.save(user);
    }

    @Override
    public void resetPasswordToDefault(String userEmail) {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        // Nếu tìm thấy người dùng với email này
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Mã hóa mật khẩu mặc định "123456"
            String defaultPassword = passwordEncoder.encode("123456");
            // Cập nhật mật khẩu cho người dùng
            user.setPassword(defaultPassword);
            // Lưu lại vào CSDL
            userRepository.save(user);
        }
        // Nếu không tìm thấy, không làm gì cả để bảo mật.
    }

    @Override
    public void changeUserPassword(String username, PasswordChangeDto dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng."));

        // 1. Kiểm tra mật khẩu cũ có khớp không
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không chính xác.");
        }

        // 2. Kiểm tra mật khẩu mới và xác nhận có khớp không
        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new RuntimeException("Mật khẩu mới và mật khẩu xác nhận không khớp.");
        }

        // 3. Cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void lockUser(Integer userIdToLock, String adminUsername) {
        User userToLock = userRepository.findById(userIdToLock)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng để khóa."));
        User adminUser = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy admin."));

        // 1. KIỂM TRA KHÔNG CHO TỰ KHÓA
        if (userToLock.getId().equals(adminUser.getId())) {
            throw new RuntimeException("Bạn không thể tự khóa tài khoản của chính mình.");
        }

        // 2. KHÓA TÀI KHOẢN TRONG CSDL
        userToLock.setLocked(true);
        userRepository.save(userToLock);

        // 3. "ĐÁ" NGƯỜI DÙNG RA KHỎI HỆ THỐNG
        // Lấy tất cả các principal (thông tin người dùng đang đăng nhập)
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object principal : principals) {
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                // Nếu tìm thấy người dùng vừa bị khóa trong danh sách đang đăng nhập
                if (userDetails.getUsername().equals(userToLock.getUsername())) {
                    // Lấy tất cả các session của người dùng đó
                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
                    // Vô hiệu hóa (expire) tất cả các session đó
                    for (SessionInformation session : sessions) {
                        session.expireNow();
                    }
                }
            }
        }
    }

    @Override
    public void unlockUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng."));
        user.setLocked(false);
        userRepository.save(user);
    }

    @Override
    public void updateUserByAdmin(AdminUserEditDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng."));

        // Cập nhật thông tin từ DTO
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());

        userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    // --- THÊM PHƯƠNG THỨC NÀY ---
    @Override
    public void deleteUser(Integer userIdToDelete, String adminUsername) {
        User userToDelete = userRepository.findById(userIdToDelete)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng để xóa."));
        User adminUser = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy admin."));

        // KIỂM TRA KHÔNG CHO TỰ XÓA
        if (userToDelete.getId().equals(adminUser.getId())) {
            throw new RuntimeException("Bạn không thể tự xóa tài khoản của chính mình.");
        }

        userRepository.delete(userToDelete);
    }

    @Override
    public Page<User> findAllUsers(Pageable pageable, String role) {
        // Nếu có tham số role được truyền vào và nó không rỗng
        if (role != null && !role.isEmpty()) {
            try {
                // Chuyển chuỗi role thành kiểu Enum
                User.Role userRole = User.Role.valueOf(role.toUpperCase());
                // Gọi phương thức mới trong repository
                return userRepository.findByRole(userRole, pageable);
            } catch (IllegalArgumentException e) {
                // Nếu role không hợp lệ, trả về tất cả user
                return userRepository.findAll(pageable);
            }
        }
        // Nếu không có role, trả về tất cả user
        return userRepository.findAll(pageable);
    }
}