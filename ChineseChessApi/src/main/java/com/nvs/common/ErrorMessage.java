package com.nvs.common;

import static com.nvs.common.Default.Game.PlayBoardSize.COL_MAX;
import static com.nvs.common.Default.Game.PlayBoardSize.COL_MIN;
import static com.nvs.common.Default.Game.PlayBoardSize.ROW_MAX;
import static com.nvs.common.Default.Game.PlayBoardSize.ROW_MIN;

public class ErrorMessage {

  public static final String ACCESS_DENIED = "Access denied";

  public static final String BLANK_DATA = "Blank";

  public static final String BET_ELO = ">= " + Default.Game.BET_ELO + "(elo)";

  public static final String COL =
      COL_MIN + " <= col <= " + COL_MAX;

  public static final String COL_LENGTH = "= " + COL_MAX;

  public static final String CUMULATIVE_TIME = ">= " + Default.Game.CUMULATIVE_TIME + "(seconds)";

  public static final String DATA_ALREADY_EXISTS = "Data already exists";

  public static final String DATA_NOT_FOUND = "Data not found";

  public static final String DEAD_PIECE = "Piece is dead";

  public static final String DEPOSIT_MILESTONES = ">= " + Default.Game.DEPOSIT_MILESTONES;

  public static final String DISABLE_USER = "User is disable";

  public static final String ELO_MILESTONES = ">= " + Default.Game.ELO_MILESTONES + "(elo)";

  public static final String END_MATCH = "Match is end";

  public static final String EXPIRED_TOKEN = "Token is expired";

  public static final String INCORRECT_DATA_LOGIN = "Incorrect phone number or password";

  public static final String INVALID_DATA = "Invalid data";

  public static final String INVALID_FILE_TYPE = "Invalid file type";

  public static final String INVALID_MOVE = "Invalid move";

  public static final String INVALID_MOVING_PLAYER = "Invalid moving player";

  public static final String INVALID_PASSWORD_SIZE =
      "Password should have at least" + Validation.PASSWORD_SIZE_MIN;

  public static final String INVALID_PLAYER_MOVE_PIECE = "Player is moving invalid piece";

  public static final String LOCKED_USER = "User is locked";

  public static final String MATCH_TIME = ">= " + Default.Game.MATCH_TIME + "(minutes)";

  public static final String MOVING_TIME = ">= " + Default.Game.MOVING_TIME + "(seconds)";

  public static final String NOT_ENOUGH_PERMISSION = "Not enough permission";

  public static final String NOT_ENOUGH_ELO = "Player doesn't enough elo";

  public static final String NULL_DATA = "null";

  public static final String OPPONENT_TURN = "Opponent's turn";

  public static final String PLAYER_PLAYING = "Player is playing in another match";

  public static final String PLAY_BOARD_SIZE = "Invalid play board size";

  public static final String ROW =
      ROW_MIN + " <= col <= " + ROW_MAX;

  public static final String ROW_LENGTH = " = " + ROW_MAX;

  public static final String INTERNAL_SERVER_ERROR = "An unexpected error occurred on the server";

  public static final String UNAUTHORIZED = "UnAuthorized";

  public static final String UNRECOGNIZED = "Data is unrecognized";

  public static final Object ERROR_OLD_PASSWORD = "Old password is incorrect";

  public static final Object ERROR_NEW_PASSWORD_CONFIRM = "New password confirm is incorrect";

}
