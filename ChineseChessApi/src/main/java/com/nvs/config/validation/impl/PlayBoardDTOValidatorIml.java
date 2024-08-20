package com.nvs.config.validation.impl;

import com.nvs.common.Default;
import com.nvs.common.enumeration.EPiece;
import com.nvs.config.exception.InvalidExceptionCustomize;
import com.nvs.config.i18nMessage.Translator;
import com.nvs.config.validation.Validator;
import com.nvs.data.dto.PieceDTO;
import com.nvs.data.dto.PlayBoardDTO;
import com.nvs.data.repository.PieceRepository;
import com.nvs.service.PieceService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PlayBoardDTOValidatorIml implements ConstraintValidator<Validator, PlayBoardDTO> {

  private static final int COL_MAX = Default.Game.PlayBoardSize.COL_MAX;
  private static final int ROW_MAX = Default.Game.PlayBoardSize.ROW_MAX;

  private final PieceRepository pieceRepository;
  private final PieceService pieceService;

  @Override
  public boolean isValid(PlayBoardDTO playBoardDTO, ConstraintValidatorContext context) {
    log.debug("-- Starting validation for PlayBoardDTO: {}", playBoardDTO);

    int colLength = playBoardDTO.getState().length - 1;
    int rowLength = playBoardDTO.getState()[0].length - 1;

    if ((COL_MAX != colLength) || (ROW_MAX != rowLength)) {
      log.error(
          "PlayBoard size mismatch: expected COL_MAX={}, ROW_MAX={}, but got colLength={}, rowLength={}",
          COL_MAX, ROW_MAX, colLength, rowLength);

      Map<String, Object> errors = new HashMap<>();
      errors.put("col", Translator.toLocale("COLUMN_LENGTH", COL_MAX));
      errors.put("row", Translator.toLocale("ROW_LENGTH", ROW_MAX));

      throw new InvalidExceptionCustomize(Translator.toLocale("PLAY_BOARD_SIZE"), errors);
    } else {
      Set<Integer> pieceIds = new HashSet<>();
      boolean existsRedGeneral = false;
      boolean existsBlackGeneral = false;

      for (int col = 0; col <= colLength; col++) {
        for (int row = 0; row <= rowLength; row++) {
          PieceDTO pieceDTO = playBoardDTO.getState()[col][row];
          if (pieceDTO != null) {
            log.debug("-- Validating piece at col={}, row={}, pieceDTO={}", col, row, pieceDTO);

            // Validate id
            if (!pieceRepository.existsById(pieceDTO.getId())) {
              log.error("Piece ID not found in repository: {}", pieceDTO.getId());
              throw new InvalidExceptionCustomize(
                  buildValidateErrors(pieceDTO, col, row, Translator.toLocale("DATA_NOT_FOUND")));
            }

            if (pieceIds.contains(pieceDTO.getId())) {
              log.error("Duplicate piece ID found: {}", pieceDTO.getId());
              throw new InvalidExceptionCustomize(buildValidateErrors(pieceDTO, col, row,
                  Translator.toLocale("DATA_ALREADY_EXISTS")));
            } else {
              pieceIds.add(pieceDTO.getId());
            }

            // Validate name
            EPiece ePiece = pieceService.convertByName(pieceDTO.getName());
            if (ePiece == null) {
              log.error("Invalid piece name: {}", pieceDTO.getName());
              throw new InvalidExceptionCustomize(
                  buildValidateErrors(pieceDTO, col, row, Translator.toLocale("DATA_NOT_FOUND")));
            }

            // Check if generals exist
            if (!existsRedGeneral || !existsBlackGeneral) {
              if (EPiece.GENERAL == ePiece) {
                if (pieceDTO.isRed()) {
                  existsRedGeneral = true;
                } else {
                  existsBlackGeneral = true;
                }
              }
            }

            // Validate index
            if ((col != pieceDTO.getCurrentCol()) || (row != pieceDTO.getCurrentRow())) {
              log.error("Invalid piece index: {} expected colInBoard={}, rowInBoard={}", pieceDTO,
                  col, row);
              throw new InvalidExceptionCustomize(buildValidateErrors(pieceDTO, col, row,
                  Translator.toLocale("PIECE_INVALID_INDEX")));
            }
          }
        }
      }

      if (!existsRedGeneral) {
        log.error("Red general not found on the board");
        throw new InvalidExceptionCustomize(Collections.singletonMap("message",
            Translator.toLocale("PIECE_NOT_FOUND_IN_BOARD", Translator.toLocale("RED_GENERAL"))));
      }

      if (!existsBlackGeneral) {
        log.error("Black general not found on the board");
        throw new InvalidExceptionCustomize(Collections.singletonMap("message",
            Translator.toLocale("PIECE_NOT_FOUND_IN_BOARD", Translator.toLocale("BLACK_GENERAL"))));
      }
    }

    log.debug("-- Validation passed for PlayBoardDTO: {}", playBoardDTO);
    return true;
  }

  private Map<String, Object> buildValidateErrors(PieceDTO pieceDTO, int col, int row,
      String message) {
    Map<String, Object> errors = new HashMap<>();
    errors.put("message", message);
    errors.put("pieceDTO", pieceDTO);
    errors.put("col", col);
    errors.put("row", row);

    return errors;
  }

}
