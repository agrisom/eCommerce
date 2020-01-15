package com.griso.shop.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class FileUtil {

    private FileUtil() {}

    // get file from classpath, resources folder
    public static File getFileFromResources(ClassLoader classLoader, String fileName) {

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }

    public static String fileToString(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        if (file == null) return "";

        try (FileReader reader = new FileReader(file);
             BufferedReader br = new BufferedReader(reader)) {

            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        }

        return content.toString();
    }

}
