package com.victorebubemadu.klasha.globalxchange;

import java.io.InputStream;

public class ResourceFilePath {

    public static class Csv {
        public static final String EXCHANGE_RATE = "static/exchange_rate.csv";
    }

    public static InputStream load(String resourceFilePath) {
        return ResourceFilePath.class.getClassLoader().getResourceAsStream(resourceFilePath);
    }

    public static String filePath(String resourceFilePath) {
        return ResourceFilePath.class.getClassLoader().getResource(resourceFilePath).getPath();
    }

}
