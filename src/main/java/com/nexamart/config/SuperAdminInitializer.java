package com.nexamart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nexamart.domain.USER_ROLE;
import com.nexamart.modal.User;
import com.nexamart.repository.UserRepository;

@Configuration
public class SuperAdminInitializer {

	@Autowired
	UserRepository userRepository;

	@Bean
	CommandLineRunner initSuperAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {

		return args -> {

			String adminEmail = "titurkarbhushan2001@gmail.com";

			User existingAdmin = userRepository.findByEmail(adminEmail);

			if (existingAdmin == null) {

				User admin = new User();
				admin.setFullName("admin");
				admin.setEmail(adminEmail);
				admin.setPassword(passwordEncoder.encode("admin"));
				admin.setRole(USER_ROLE.ROLE_ADMIN);

				userRepository.save(admin);

				System.out.println("Super Admin Created");
			} else if (existingAdmin.getRole() != USER_ROLE.ROLE_ADMIN) {
				existingAdmin.setRole(USER_ROLE.ROLE_ADMIN);
				userRepository.save(existingAdmin);
				System.out.println("Super Admin Role Updated to ADMIN");
			}
		};
	}
}
