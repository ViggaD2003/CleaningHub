package com.fpu.exe.cleaninghub;

import com.fpu.exe.cleaninghub.entity.Role;
import com.fpu.exe.cleaninghub.entity.User;
import com.fpu.exe.cleaninghub.repository.RoleRepository;
import com.fpu.exe.cleaninghub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@RequiredArgsConstructor
public class CleaninghubApplication implements CommandLineRunner {
	@Autowired
	private final UserRepository userRepository;

	@Autowired
	private final RoleRepository roleRepo;


	public static void main(String[] args) {
		SpringApplication.run(CleaninghubApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (roleRepo.count() == 0) {
			List<Role> roles = new ArrayList<>();
			Role adminRole = Role.builder()
					.id(1)
					.name("ROLE_ADMIN")
					.build();
			Role userRole = Role.builder()
					.id(2)
					.name("ROLE_USER")
					.build();
			Role staffRole = Role.builder()
					.id(3)
					.name("ROLE_STAFF")
					.build();
			roles.add(adminRole);
			roles.add(userRole);
			roles.add(staffRole);
			roleRepo.saveAll(roles);
		}

		Role role = roleRepo.findById(1).orElseThrow();
		User adminAccount = userRepository.findByRole(role);

		if(null == adminAccount ){
			User user = new User();
			user.setCreateBy(0);
			user.setEmail("admin@gmail.com");
			user.setRole(role);
			user.setPassword(new BCryptPasswordEncoder().encode("123"));
			userRepository.save(user);
		}
	}
}
