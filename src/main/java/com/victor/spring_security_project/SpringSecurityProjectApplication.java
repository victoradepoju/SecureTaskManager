package com.victor.spring_security_project;

import com.victor.spring_security_project.role.Role;
import com.victor.spring_security_project.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
@EnableJpaRepositories(basePackages = {
		"com.victor.spring_security_project.role",
		"com.victor.spring_security_project.task",
		"com.victor.spring_security_project.token",
		"com.victor.spring_security_project.user"
})
public class SpringSecurityProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityProjectApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(
						Role.builder().name("USER").build()
				);
			}
			if (roleRepository.findByName("ADMIN").isEmpty()) {
				roleRepository.save(
						Role.builder().name("ADMIN").build()
				);
			}
		};
	}
}
