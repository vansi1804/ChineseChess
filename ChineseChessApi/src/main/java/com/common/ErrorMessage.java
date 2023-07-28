package com.common;

public class ErrorMessage {

    public static final String INVALID_DATA = "Invalid data";

    public static final String INVALID_MOVING_PLAYER = "Invalid moving player";

    public static final String INVALID_MOVE = "Invalid move";

    public static final String PIECE_NOT_MOVE = "Piece is not moving";

    public static final String INVALID_PLAYER_MOVE_PIECE = "Player is moving invalid piece";

    public static final String INVALID_JSON_PAYLOAD = "Json invalid payload";

    public static final String PLAYER_PLAYING = "Player is playing in another match";

    public static final String END_MATCH = "Match is end";

    public static final String DEAD_PIECE = "Piece is dead";

    public static final String OPPONENT_TURN = "Opponent's turn";

    public static final String DATA_NOT_FOUND = "Data not found";

    public static final String DATA_ALREADY_EXISTS = "Data already exists";

    public static final String BLANK_DATA = "Data is blank";

    public static final String NULL_DATA = "Data is null";

    public static final String INVALID_PASSWORD_SIZE = "Password should have at least"
            + Validation.PASSWORD_SIZE_MIN + " characters";

    public static final String INVALID_FILE_TYPE = "Invalid file type";

    public static final String ACCESS_DENIED = "Access denied";

    public static final String NOT_ENOUGH_PERMISSION = "Not enough permission";

    public static final String UNAUTHORIZED = "UnAuthorized";

    public static final String SERVER_ERROR = "An unexpected error occurred on the server";

    public static final String EXPIRED_TOKEN = "Token is expired";

    public static final String LOCKED_USER = "User is locked";

    public static final String DISABLE_USER = "User is disable";

    public static final String COL = (Validation.AREA_MIN - 1) + " <= col <= " + (Validation.COL_MAX - 1);

    public static final String ROW = (Validation.AREA_MIN - 1) + " <= row <= " + (Validation.ROW_MAX - 1);
    
    public static final String COL_LENGTH = " = " + Validation.COL_MAX ;
    
    public static final String ROW_LENGTH = " = " + Validation.ROW_MAX;

    public static final String PLAY_BOARD_SIZE = "Invalid play board size";

    public static final String MATCH_TIME = "null or >= " + Validation.MATCH_TIME + "(minutes)";

    public static final String MOVING_TIME = "null or >= " + Validation.MOVING_TIME + "(seconds)";

    public static final String CUMULATIVE_TIME = "null or >= " + Validation.CUMULATIVE_TIME + "(seconds)";

    public static final String BET_ELO = ">= " + Validation.BET_ELO + "(elo)";

    public static final String ELO_MILESTONES = ">= " + Validation.ELO_MILESTONES + "(elo)";

    public static final String DEPOSIT_MILESTONES = ">= " + Validation.DEPOSIT_MILESTONES;

}
