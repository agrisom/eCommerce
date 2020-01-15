package com.griso.shop;

import com.griso.shop.entities.UserDB;
import com.griso.shop.repository.IUserRepo;
import com.griso.shop.util.Constants;
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
	void checkUserEntity_ADMIN() {
		Optional<UserDB> user = userRepo.findByUsername("admin");
		if(!user.isPresent()) {
			user = Optional.of(createAdminUser());
		}
		assertTrue(user.isPresent());
		assertNotNull(user);
		assertNotNull(user.get().getId());
	}

	@Test
	void checkUserEntity_USER() {
		Optional<UserDB> user = userRepo.findByUsername("albert.griso.mendez@gmail.com");
		if(!user.isPresent()) {
			user = Optional.of(createUser());
		}
		assertTrue(user.isPresent());
		assertNotNull(user);
		assertNotNull(user.get().getId());
	}

	private UserDB createAdminUser() {
		UserDB userDB = new UserDB();
		userDB.setUsername("admin");
		userDB.setPassword(new BCryptPasswordEncoder().encode("admin"));
		userDB.setName("Admin");
		userDB.setRoles(Constants.ROLE.ADMIN);
		userDB.setActive(true);
		return userRepo.save(userDB);
	}

	private UserDB createUser() {
		UserDB userDB = new UserDB();
		userDB.setUsername("albert.griso.mendez@gmail.com");
		userDB.setPassword(new BCryptPasswordEncoder().encode("1234"));
		userDB.setName("Albert");
		userDB.setSurname("Griso Mendez");
		Calendar calendar = Calendar.getInstance();
		calendar.set(1992, Calendar.JULY, 25);
		userDB.setBirthday(calendar.getTime());
		userDB.setActive(true);
		return userRepo.save(userDB);
	}

}
