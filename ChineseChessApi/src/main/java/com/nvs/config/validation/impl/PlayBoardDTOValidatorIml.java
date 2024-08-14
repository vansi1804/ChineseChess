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
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayBoardDTOValidatorIml implements ConstraintValidator<Validator, PlayBoardDTO> {

  private static final int COL_MAX = Default.Game.PlayBoardSize.COL_MAX;
  private static final int ROW_MAX = Default.Game.PlayBoardSize.ROW_MAX;

  private final PieceRepository pieceRepository;
  private final PieceService pieceService;

  @Override
  public boolean isValid(PlayBoardDTO playBoardDTO, ConstraintValidatorContext context) {
    int colLength = playBoardDTO.getState().length - 1;
    int rowLength = playBoardDTO.getState()[0].length - 1;

    if ((COL_MAX != colLength) || (ROW_MAX != rowLength)) {
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
            // validate id
            if (!pieceRepository.existsById(pieceDTO.getId())) {
              throw new InvalidExceptionCustomize(
                  buildValidateErrors(pieceDTO, col, row, Translator.toLocale("DATA_NOT_FOUND")));
            }

            if (pieceIds.contains(pieceDTO.getId())) {
              throw new InvalidExceptionCustomize(buildValidateErrors(pieceDTO, col, row,
                  Translator.toLocale("DATA_ALREADY_EXISTS")));
            } else {
              pieceIds.add(pieceDTO.getId());
            }

            // validate name
            EPiece ePiece = pieceService.convertByName(pieceDTO.getName());
            if (ePiece == null) {
              throw new InvalidExceptionCustomize(
                  buildValidateErrors(pieceDTO, col, row, Translator.toLocale("DATA_NOT_FOUND")));
            }

            // check exists generals
            if (!existsRedGeneral || !existsBlackGeneral) {
              if (EPiece.GENERAL == ePiece) {
                if (pieceDTO.isRed()) {
                  existsRedGeneral = true;
                } else {
                  existsBlackGeneral = true;
                }
              }
            }

            // validate index
            if ((col != pieceDTO.getCurrentCol()) || (row != pieceDTO.getCurrentRow())) {
              throw new InvalidExceptionCustomize(buildValidateErrors(pieceDTO, col, row,
                  Translator.toLocale("PIECE_INVALID_INDEX")));
            }
          }
        }
      }

      if (!existsRedGeneral) {
        throw new InvalidExceptionCustomize(
            Collections.singletonMap("message",
                Translator.toLocale("PIECE_NOT_FOUND_IN_BOARD",
                    Translator.toLocale("RED_GENERAL"))));
      }

      if (!existsBlackGeneral) {
        throw new InvalidExceptionCustomize(
            Collections.singletonMap("message",
                Translator.toLocale("PIECE_NOT_FOUND_IN_BOARD",
                    Translator.toLocale("BLACK_GENERAL"))));
      }
    }

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
