package com.griso.shop;

import com.griso.shop.entities.User;
import com.griso.shop.repository.IUserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Calendar;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ShopApplicationTests {

	@Autowired
	private IUserRepo userRepo;

	@Test
	void contextLoads() {
	}

	@Test
	void checkUserEntity_ADMIN() {
		Optional<User> user = userRepo.findByUsername("admin");
		if(!user.isPresent()) {
			user = Optional.of(createAdminUser());
		}
		assertTrue(user.isPresent());
		assertNotNull(user);
		assertNotNull(user.get().getId());
	}

	@Test
	void checkUserEntity_USER() {
		Optional<User> user = userRepo.findByUsername("albert.griso.mendez@gmail.com");
		if(!user.isPresent()) {
			user = Optional.of(createUser());
		}
		assertTrue(user.isPresent());
		assertNotNull(user);
		assertNotNull(user.get().getId());
	}

	private User createAdminUser() {
		User user = new User();
		user.setUsername("admin");
		user.setPassword(new BCryptPasswordEncoder().encode("admin"));
		user.setName("Admin");
		user.setRoles("ADMIN");
		user.setActive(true);
		return userRepo.save(user);
	}

	private User createUser() {
		User user = new User();
		user.setUsername("albert.griso.mendez@gmail.com");
		user.setPassword(new BCryptPasswordEncoder().encode("admin"));
		user.setName("Albert");
		user.setSurname("Griso Mendez");
		Calendar calendar = Calendar.getInstance();
		calendar.set(1992, 07, 25);
		user.setBirthday(calendar.getTime());
		user.setActive(true);
		return userRepo.save(user);
	}

}
