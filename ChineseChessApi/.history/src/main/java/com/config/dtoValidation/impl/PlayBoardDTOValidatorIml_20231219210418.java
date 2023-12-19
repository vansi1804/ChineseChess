package com.config.dtoValidation.impl;

import com.common.Default;
import com.common.ErrorMessage;
import com.common.enumeration.EPiece;
import com.config.dtoValidation.Validator;
import com.config.exception.InvalidExceptionCustomize;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.data.repository.PieceRepository;
import com.service.PieceService;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayBoardDTOValidatorIml
  implements ConstraintValidator<Validator, PlayBoardDTO> {

  private final PieceRepository pieceRepository;
  private final PieceService pieceService;

  @Override
  public boolean isValid(
    PlayBoardDTO playBoardDTO,
    ConstraintValidatorContext context
  ) {
    int colLength = playBoardDTO.getState().length - 1;
    int rowLength = playBoardDTO.getState()[0].length - 1;

    if (
      (Default.Game.PlayBoardSize.COL_MAX != colLength) ||
      (Default.Game.PlayBoardSize.ROW_MAX != rowLength)
    ) {
      Map<String, Object> errors = new HashMap<>();
      errors.put("colLength", ErrorMessage.COL_LENGTH);
      errors.put("rowLength", ErrorMessage.ROW_LENGTH);

      throw new InvalidExceptionCustomize(ErrorMessage.PLAY_BOARD_SIZE, errors);
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
                buildValidateErrors(
                  pieceDTO,
                  col,
                  row,
                  "Not found pieceDTO.id: " + pieceDTO.getId()
                )
              );
            }

            if (pieceIds.contains(pieceDTO.getId())) {
              throw new InvalidExceptionCustomize(
                buildValidateErrors(
                  pieceDTO,
                  col,
                  row,
                  "Existing pieceDTO.id: " + pieceDTO.getId()
                )
              );
            } else {
              pieceIds.add(pieceDTO.getId());
            }

            // validate name
            EPiece ePiece = pieceService.convertByName(pieceDTO.getName());
            if (ePiece == null) {
              throw new InvalidExceptionCustomize(
                buildValidateErrors(
                  pieceDTO,
                  col,
                  row,
                  "Not found pieceDTO.name: " + pieceDTO.getId()
                )
              );
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
            if (
              (col != pieceDTO.getCurrentCol()) ||
              (row != pieceDTO.getCurrentRow())
            ) {
              throw new InvalidExceptionCustomize(
                buildValidateErrors(pieceDTO, col, row, "Error index")
              );
            }
          }
        }
      }

      if (!existsRedGeneral) {
        throw new InvalidExceptionCustomize(
          Collections.singletonMap(
            "message",
            "Red general is not found in board"
          )
        );
      }

      if (!existsBlackGeneral) {
        throw new InvalidExceptionCustomize(
          Collections.singletonMap(
            "message",
            "Black general is not found in board"
          )
        );
      }
    }

    return true;
  }

  private Map<String, Object> buildValidateErrors(
    PieceDTO pieceDTO,
    int col,
    int row,
    String message
  ) {
    Map<String, Object> errors = new HashMap<>();
    errors.put("message", message);
    errors.put("pieceDTO", pieceDTO);
    errors.put("col", col);
    errors.put("row", row);

    return errors;
  }
}
