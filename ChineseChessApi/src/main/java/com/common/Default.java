package com.common;

import com.common.enumeration.ERole;
import com.common.enumeration.EStatus;

public class Default {

    public static class DateTimeFormat {

        public static final String DATE = "dd-MM-yyyy";

        public static final String DATE_TIME = DATE + " HH:mm:ss";

    }

    public static class Page {

        public static final String NO = "0";

        public static final String LIMIT = "20";

        public static final String SORT_BY = "id";

    }

    public static class User {
        public static final EStatus STATUS = EStatus.INACTIVE;

        public static class Admin {

            public static final String PHONE_NUMBER = "0589176839";

            public static final String PASSWORD = "admin";

            public static final String NAME = "Nguyễn Văn Sĩ";

            public static final ERole ROLE = ERole.ADMIN;

            public static final EStatus STATUS = EStatus.ACTIVE;

        }

    }

    public static class JWT {

        public static final String SECRET = "V2hlbiB5b3UgYXJlIGdvaW5nIHRvIGdpdmUgdXAsIHRoaW5rIGFib3V0IHRoZSByZWFzb24gbWFkZSB5b3Ugc3RhclQNCg==";

        public static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60;

        public static final long REFRESH_EXPIRATION_TIME = ACCESS_TOKEN_EXPIRATION_TIME * 24;

        public static final String TOKEN_PREFIX = "Bearer" + " ";

        public static final String HEADER_STRING = "Authorization";

    }

    public static class Game {

        // 1__2__3__4__5__6__7__8__9
        // +--+--+--+--+--+--+--+--+ 1
        // |..|..|..|\ | /|..|..|..|
        // +--+--+--+--+--+--+--+--+ 2
        // |..|..|..|/ | \|..|..|..|
        // +--+--+--+--+--+--+--+--+ 3
        // |..|..|..|..|..|..|..|..|
        // +--+--+--+--+--+--+--+--+ 4
        // |..|..|..|..|..|..|..|..|
        // +--+--+--+--+--+--+--+--+ 5
        // |~~|~~|~~|~~|~~|~~|~~|~~|
        // +--+--+--+--+--+--+--+--+ 6
        // |..|..|..|..|..|..|..|..|
        // +--+--+--+--+--+--+--+--+ 7
        // |..|..|..|..|..|..|..|..|
        // +--+--+--+--+--+--+--+--+ 8
        // |..|..|..|\ | /|..|..|..|
        // +--+--+--+--+--+--+--+--+ 9
        // |..|..|..|/ | \|..|..|..|
        // +--+--+--+--+--+--+--+--+ 10

        public static class PlayBoardSize {

            public static final int AREA_MIN = 1;

            public static final int COL_MAX = 9;

            public static final int ROW_MAX = 10;

            public static final int CENTER_COL_MIN = 4;

            public static final int CENTER_COL_MAX = 6;

            public static class BlackArea {

                public static final int ROW_MAX = 5;

                public static final int CENTER_ROW_MIN = 1;

                public static final int CENTER_ROW_MAX = 3;

            }

            public static class RedArea {

                public static final int ROW_MIN = 6;

                public static final int CENTER_ROW_MIN = 8;

                public static final int CENTER_ROW_MAX = 10;

            }

        }

        public static final float ELO_WIN_RECEIVE_PERCENT = 0.95F;

    }

}
