package com.griso.shop.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilTest {

    @Test
    void getFileFromResources() {
        File template = FileUtil.getFileFromResources(getClass().getClassLoader(),"template/welcomeEmailTemplate.html");
        assertNotNull(template);
    }

    @Test
    void getFileFromResources_EXCEPTION() {
        File template = null;
        try{
            template = FileUtil.getFileFromResources(getClass().getClassLoader(),"template/noFile.html");
        } catch (IllegalArgumentException e) {
            assertNotNull(e);
        }
        assertNull(template);
    }

    @Test
    void fileToString() throws IOException {
        File template = FileUtil.getFileFromResources(getClass().getClassLoader(),"template/welcomeEmailTemplate.html");
        String content = FileUtil.fileToString(template);

        assertNotNull(content);
        assertFalse(content.isEmpty());
    }
}