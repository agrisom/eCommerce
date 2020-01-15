package com.griso.shop.util;

public class Constants {

    private Constants() {}

    public static class ENDPOINT {

        private ENDPOINT() {}

        public static final String URL_ADMIN = "/admin";
        public static final String URL_USER = "/user";
        public static final String URL_USER_ADMIN = "/admin/user";
    }

    public static class ROLE {

        private ROLE() {}

        public static final String ADMIN = "ADMIN";
        public static final String USER = "USER";
        public static final String MANAGER = "MANAGER";
    }

    public static class ERROR {

        private ERROR() {}

        public static final String UNAUTHORIZED = "Unauthorized";
    }


}
