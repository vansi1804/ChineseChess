package com.nvs.common;

import com.nvs.common.enumeration.ERole;
import com.nvs.common.enumeration.EStatus;

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

    public static final int MATCH_TIME = 5; // in minutes
    public static final int MOVING_TIME = 30; // in seconds
    public static final int CUMULATIVE_TIME = 3; // in seconds
    public static final int BET_ELO = 0;
    public static final int ELO_MILESTONES = 0;
    public static final int DEPOSIT_MILESTONES = 0;
    public static final float ELO_WIN_RECEIVE_PERCENT = 0.95F;

    public static class PlayBoardSize {

      // 0_1__2__3__4__5__6__7__8
      // +--+--+--+--+--+--+--+--+ 0
      // |::|::|::|\ | /|::|::|::|
      // +--+--+--+--+--+--+--+--+ 1
      // |::|::|::|/ | \|::|::|::|
      // +--+--+--+--+--+--+--+--+ 2
      // |::|::|::|::|::|::|::|::|
      // +--+--+--+--+--+--+--+--+ 3
      // |::|::|::|::|::|::|::|::|
      // +--+--+--+--+--+--+--+--+ 4
      // |~~|~~|~~|~~|~~|~~|~~|~~|
      // +--+--+--+--+--+--+--+--+ 5
      // |::|::|::|::|::|::|::|::|
      // +--+--+--+--+--+--+--+--+ 6
      // |::|::|::|::|::|::|::|::|
      // +--+--+--+--+--+--+--+--+ 7
      // |::|::|::|\ | /|::|::|::|
      // +--+--+--+--+--+--+--+--+ 8
      // |::|::|::|/ | \|::|::|::|
      // +--+--+--+--+--+--+--+--+ 9

      public static final int COL_MIN = 0;

      public static final int ROW_MIN = 0;

      public static final int COL_MAX = 8;

      public static final int ROW_MAX = 9;

      public static final int CENTER_COL_MIN = 3;

      public static final int CENTER_COL_MAX = 5;

      public static class BlackArea {

        public static final int ROW_MAX = 4;

        public static final int CENTER_ROW_MIN = 0;

        public static final int CENTER_ROW_MAX = 2;
      }

      public static class RedArea {

        public static final int ROW_MIN = 5;

        public static final int CENTER_ROW_MIN = 7;

        public static final int CENTER_ROW_MAX = 9;
      }
    }
  }

}
