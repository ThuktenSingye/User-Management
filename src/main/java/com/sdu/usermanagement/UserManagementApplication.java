package com.sdu.usermanagement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sdu.usermanagement.model.Role;
import com.sdu.usermanagement.model.User;
import com.sdu.usermanagement.model.User.UserStatus;
import com.sdu.usermanagement.repository.RoleRepository;
import com.sdu.usermanagement.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@Log4j2
public class UserManagementApplication implements CommandLineRunner {
	

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;
	public static void main(String[] args) {
		SpringApplication.run(UserManagementApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		List<Role> roles = new ArrayList<>();
		List<User> users = userRepository.findUsersWithAdminRole(1);
		log.info(("user lenght with admin role: "+ users.size()));
		if ( users.isEmpty()) {
			log.info("There is no user with admin role");
			Role role = roleRepository.findById(1).orElseThrow();
			roles.add(role);	
			User user = new User();
			user.setEmail("admin@gmail.com");
			user.setEmployeeId(123);
			user.setCidNo(141);
			user.setFirstName("admin");
			user.setMiddleName(null);
			user.setLastName("admin");
			user.setRoles(roles);
			user.setStatus(UserStatus.ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			
			userRepository.saveAndFlush(user);
			
		}
		
		
	}

}
