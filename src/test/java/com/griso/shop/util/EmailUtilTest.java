package com.griso.shop.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailUtilTest {

    @Autowired
    private EmailUtil emailUtil;

    @Test
    void isValid_TRUE() {
        assertTrue(emailUtil.isValid("test@junit.com"));
        assertTrue(emailUtil.isValid("test@junit.co"));
        assertTrue(emailUtil.isValid("user.one_1@test.co"));
    }

    @Test
    void isValid_FALSE() {
        assertFalse(emailUtil.isValid("junit.com"));
        assertFalse(emailUtil.isValid("a@junit.c"));
        assertFalse(emailUtil.isValid("a@.com"));
        assertFalse(emailUtil.isValid("asd@com"));
    }
}