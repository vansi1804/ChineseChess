package com.common;

import com.common.enumeration.ERank;
import com.common.enumeration.EStatus;
import com.common.enumeration.EVip;

public class Default {

    public static class DateTimeFormat {
        public static final String DATE = "dd-MM-yyyy";
        public static final String DATE_TIME = DATE + " HH:mm:ss";
    }

    public static class Game {
        public class PlayBoardSize {
            public static final int AREA_Min = 1;
            public static final int COL_MAX = 9;
            public static final int ROW_MAX = 10;
        }
    }

    public static class Page {
        public static final String NO = "0";
        public static final String LIMIT = "20";
        public static final String SORT_BY = "id";
    }

    public static class User {
        // default for creating
        public static final EStatus STATUS = EStatus.LOCK;
        public static final ERank LEVEL = ERank.NOVICE;
        public static final EVip VIP = EVip.VIP0;
    }

    public static class JWT {
        public static final String SECRET = "SECRET_KEY";
        public static final long EXPIRATION_TIME = 900_000;
        public static final String TOKEN_PREFIX = "Bearer ";
        public static final String HEADER_STRING = "Authorization";
    }

}
