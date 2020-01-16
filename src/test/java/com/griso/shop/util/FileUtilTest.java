package com.griso.shop.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileUtilTest {

    @Test
    void getFileFromResources() throws IOException {
        String template = FileUtil.getFileFromResources(getClass().getClassLoader(),"template/welcomeEmailTemplate.html");
        assertNotNull(template);
        assertFalse(template.isEmpty());
    }

    @Test
    void getFileFromResources_EXCEPTION() {
        String template = null;
        try{
            template = FileUtil.getFileFromResources(getClass().getClassLoader(),"template/noFile.html");
        } catch (IOException e) {
            assertNotNull(e);
        }
        assertNull(template);
    }
}