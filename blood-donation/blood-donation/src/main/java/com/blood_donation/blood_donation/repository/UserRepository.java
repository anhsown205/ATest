package com.blood_donation.blood_donation.repository;

import com.blood_donation.blood_donation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Dùng để kiểm tra đăng nhập và cho Spring Security
    Optional<User> findByUsername(String username);

    // Dùng để kiểm tra khi đăng ký xem username đã tồn tại chưa
    boolean existsByUsername(String username);

    // Dùng để kiểm tra khi đăng ký xem email đã tồn tại chưa
    boolean existsByEmail(String email);
    // Dùng để kiểm tra đăng nhập và cho Spring Security
    Optional<User> findByEmail(String username);
}