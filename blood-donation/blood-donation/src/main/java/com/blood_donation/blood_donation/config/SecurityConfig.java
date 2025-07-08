package com.blood_donation.blood_donation.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.blood_donation.blood_donation.service.UserDetailsServiceImpl;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public paths
                        .requestMatchers("/", "/home", "/login", "/register", "/css/**", "/js/**", "/images/**", "/error").permitAll()
                        .requestMatchers("/blogs/**", "/blood-info").permitAll()
                        // Admin specific paths
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        // Admin can also manage their own profile
                        .requestMatchers("/profile/**").authenticated() // All authenticated users can manage their profile
                        // Staff specific paths
                        .requestMatchers("/staff/**").hasAnyAuthority("STAFF", "ADMIN") // Admin also has staff privileges
                        // Staff can also manage their own profile
                        .requestMatchers("/donations/register", "/donations/history", "/donations/edit/**", "/donations/update").hasAnyAuthority("MEMBER", "STAFF", "ADMIN") // Staff can also register/view donations
                        .requestMatchers("/requests/emergency/new", "/requests/emergency").hasAnyAuthority("MEMBER", "STAFF", "ADMIN") // Staff can also create emergency requests
                        // Member specific paths
                        .requestMatchers("/dashboard").authenticated() // All authenticated users can access dashboard
                        .requestMatchers("/donations/register", "/donations/history", "/donations/edit/**", "/donations/update").hasAuthority("MEMBER")
                        .requestMatchers("/requests/emergency/new", "/requests/emergency").hasAuthority("MEMBER")
                        .requestMatchers("/profile/**").hasAuthority("MEMBER") // Member can manage their own profile
                        // General authenticated paths (if not covered by specific roles above)
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/perform_logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );
        return http.build();
    }
}