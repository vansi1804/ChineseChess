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
            public static final int COL = 9;
            public static final int ROW = 10;
        }
    }

    public static class Page {
        public static final String NO = "1";
        public static final String LIMIT = "20";
        public static final String SORT_BY = "id";
    }

    public static class User {
        // default for creating
        public static final EStatus STATUS = EStatus.LOCK;
        public static final ERank LEVEL = ERank.NOVICE;
        public static final EVip VIP = EVip.VIP0;
    }

}
