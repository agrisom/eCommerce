package com.griso.shop.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailUtilTest {

    @Test
    void isValid_TRUE() {
        assertTrue(EmailUtil.isValid("test@junit.com"));
        assertTrue(EmailUtil.isValid("test@junit.co"));
        assertTrue(EmailUtil.isValid("user.one_1@test.co"));
    }

    @Test
    void isValid_FALSE() {
        assertFalse(EmailUtil.isValid("junit.com"));
        assertFalse(EmailUtil.isValid("a@junit.c"));
        assertFalse(EmailUtil.isValid("a@.com"));
        assertFalse(EmailUtil.isValid("asd@com"));
    }
}