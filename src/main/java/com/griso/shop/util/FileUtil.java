package com.griso.shop.util;

import java.io.*;

public class FileUtil {

    private FileUtil() {}

    public static String getFileFromResources(ClassLoader classLoader, String fileName) throws IOException {

        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if(inputStream == null) {
            throw new IOException("File not found");
        }
        InputStreamReader isReader = new InputStreamReader(inputStream);

        BufferedReader reader = new BufferedReader(isReader);
        StringBuffer sb = new StringBuffer();
        String str;
        while((str = reader.readLine())!= null){
            sb.append(str);
        }
        return sb.toString();
    }

}
