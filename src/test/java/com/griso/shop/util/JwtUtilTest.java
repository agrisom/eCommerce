package com.griso.shop.util;

import com.griso.shop.dto.UserDto;
import com.griso.shop.model.UserSecurity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    JwtUtil jwtUtil;

    @Test
    void generateToken() {
        UserDto user = new UserDto();
        user.setUsername("albert");
        UserDetails userDetails = new UserSecurity(user);
        String token = jwtUtil.generateToken(userDetails);
        String userName = jwtUtil.extractUsername(token);

        assertEquals(user.getUsername(), userName);
    }
}