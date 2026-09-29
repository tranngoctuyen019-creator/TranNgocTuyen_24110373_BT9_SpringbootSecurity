package vn.iotstar;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.*;
import vn.iotstar.repository.*;

@SpringBootApplication
public class Springboot18Application {
    public static void main(String[] args) { SpringApplication.run(Springboot18Application.class, args); }

    @Bean
    CommandLineRunner init(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_ADMIN").build()));

            if (userRepository.findByUsername("ngoctuyen").isEmpty()) {
                userRepository.save(User.builder().username("ngoctuyen").email("ngoctuyen@example.com")
                    .password(passwordEncoder.encode("123456")).fullName("Người dùng mẫu")
                    .images("/images/user.jpg").role(userRole).enabled(true).build());
            }
            if (userRepository.findByUsername("admin").isEmpty()) {
                userRepository.save(User.builder().username("admin").email("admin@example.com")
                    .password(passwordEncoder.encode("admin123")).fullName("Quản trị viên")
                    .images("/images/avatar-default.jpg").role(adminRole).enabled(true).build());
            }
        };
    }
}
