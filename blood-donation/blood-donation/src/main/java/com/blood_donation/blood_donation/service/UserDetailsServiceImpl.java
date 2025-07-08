package com.blood_donation.blood_donation.service;

import com.blood_donation.blood_donation.entity.User;
import com.blood_donation.blood_donation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    /**
     * Phương thức này được Spring Security gọi khi một người dùng cố gắng đăng nhập.
     * Nó tìm người dùng trong CSDL bằng username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tìm user trong DB
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với username: " + username));

        // Trả về một đối tượng UserDetails mà Spring Security có thể hiểu
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(), // Mật khẩu đã được mã hóa trong DB
                // Chuyển đổi Role của chúng ta thành Authority mà Spring Security hiểu
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }

}