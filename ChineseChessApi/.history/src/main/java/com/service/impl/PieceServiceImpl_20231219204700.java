package com.service.impl;

import com.common.Default;
import com.common.enumeration.EPiece;
import com.config.exception.ResourceNotFoundExceptionCustomize;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.data.mapper.PieceMapper;
import com.data.repository.PieceRepository;
import com.service.PieceService;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PieceServiceImpl implements PieceService {

  private final PieceRepository pieceRepository;
  private final PieceMapper pieceMapper;

  @Override
  public List<PieceDTO> findAll() {
    return pieceRepository
      .findAll()
      .stream()
      .map(p -> pieceMapper.toDTO(p))
      .collect(Collectors.toList());
  }

  @Override
  public PieceDTO findById(int id) {
    return pieceRepository
      .findById(id)
      .map(p -> pieceMapper.toDTO(p))
      .orElseThrow(() ->
        new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("id", id)
        )
      );
  }

  @Override
  public EPiece convertByName(String name) {
    try {
      return EPiece.valueOf(name);
    } catch (IllegalArgumentException | NullPointerException e) {
      return null;
    }
  }

  @Override
  public List<PieceDTO> findAllInBoard(
    PlayBoardDTO playBoardDTO,
    String name,
    Boolean isRed,
    int fromCol,
    int fromRow,
    int toCol,
    int toRow
  ) {
    return IntStream
      .rangeClosed(fromCol, toCol)
      .boxed()
      .flatMap(col ->
        IntStream
          .rangeClosed(fromRow, toRow)
          .filter(row -> {
            PieceDTO pieceDTO = playBoardDTO.getState()[col][row];

            return (
              (pieceDTO != null) &&
              (StringUtils.isBlank(name) || pieceDTO.getName().equals(name)) &&
              ((isRed == null) || (pieceDTO.isRed() == isRed))
            );
          })
          .mapToObj(row -> playBoardDTO.getState()[col][row])
      )
      .toList();
  }

  @Override
  public List<PieceDTO> findAllInBoard(
    PlayBoardDTO playBoardDTO,
    String name,
    Boolean isRed
  ) {
    int fromCol = 0;
    int fromRow = 0;
    int toCol = playBoardDTO.getState().length - 1;
    int toRow = playBoardDTO.getState()[0].length - 1;

    return findAllInBoard(
      playBoardDTO,
      name,
      isRed,
      fromCol,
      fromRow,
      toCol,
      toRow
    );
  }

  @Override
  public List<PieceDTO> findAllNotInPlayBoard(PlayBoardDTO playBoardDTO) {
    List<PieceDTO> piecesInBoard = findAllInBoard(playBoardDTO, null, null);
    List<PieceDTO> deadPieces = findAll();

    // remove all existing pieces in board
    deadPieces.removeIf(deadPiece ->
      piecesInBoard
        .stream()
        .map(alivePiece -> alivePiece.getId())
        .toList()
        .contains(deadPiece.getId())
    );

    return deadPieces;
  }

  @Override
  public PieceDTO findOneInBoard(PlayBoardDTO playBoardDTO, int id) {
    int fromCol = 0;
    int fromRow = 0;
    int toCol = playBoardDTO.getState().length - 1;
    int toRow = playBoardDTO.getState()[0].length - 1;

    return IntStream
      .rangeClosed(fromCol, toCol)
      .boxed()
      .flatMap(col ->
        IntStream
          .rangeClosed(fromRow, toRow)
          .filter(row ->
            (playBoardDTO.getState()[col][row] != null) &&
            (playBoardDTO.getState()[col][row].getId() == id)
          )
          .mapToObj(row -> playBoardDTO.getState()[col][row])
      )
      .findFirst()
      .orElse(null);
  }

  @Override
  public PieceDTO findExistingTheSameInColPath(
    PlayBoardDTO playBoardDTO,
    PieceDTO pieceDTO
  ) {
    int col = pieceDTO.getCurrentCol();
    int fromRow = 0;
    int toRow = playBoardDTO.getState()[0].length - 1;

    return IntStream
      .rangeClosed(fromRow, toRow)
      .filter(row -> {
        PieceDTO currentPiece = playBoardDTO.getState()[col][row];

        return (
          (currentPiece != null) &&
          (currentPiece.getId() != pieceDTO.getId()) &&
          (currentPiece.isRed() == pieceDTO.isRed()) &&
          currentPiece.getName().equals(pieceDTO.getName())
        );
      })
      .mapToObj(row -> playBoardDTO.getState()[col][row])
      .findFirst()
      .orElse(null);
  }

  @Override
  public boolean existsBetweenInRowPath(
    PlayBoardDTO playBoardDTO,
    int currentRow,
    int fromCol,
    int toCol
  ) {
    int startCol = Math.min(fromCol, toCol) + 1;
    int endCol = Math.max(fromCol, toCol) - 1;

    return IntStream
      .rangeClosed(startCol, endCol)
      .anyMatch(col -> playBoardDTO.getState()[col][currentRow] != null);
  }

  @Override
  public boolean existsBetweenInColPath(
    PlayBoardDTO playBoardDTO,
    int currentCol,
    int fromRow,
    int toRow
  ) {
    int startRow = Math.min(fromRow, toRow) + 1;
    int endRow = Math.max(fromRow, toRow) - 1;

    return IntStream
      .rangeClosed(startRow, endRow)
      .anyMatch(row -> playBoardDTO.getState()[currentCol][row] != null);
  }

  @Override
  public int countBetweenInRowPath(
    PlayBoardDTO playBoardDTO,
    int currentRow,
    int fromCol,
    int toCol
  ) {
    int startCol = Math.min(fromCol, toCol) + 1;
    int endCol = Math.max(fromCol, toCol) - 1;

    return (int) IntStream
      .rangeClosed(startCol, endCol)
      .filter(col -> playBoardDTO.getState()[col][currentRow] != null)
      .count();
  }

  @Override
  public int countBetweenInColPath(
    PlayBoardDTO playBoardDTO,
    int currentCol,
    int fromRow,
    int toRow
  ) {
    int startRow = Math.min(fromRow, toRow) + 1;
    int endRow = Math.max(fromRow, toRow) - 1;

    return (int) IntStream
      .rangeClosed(startRow, endRow)
      .filter(row -> playBoardDTO.getState()[currentCol][row] != null)
      .count();
  }

  @Override
  public PieceDTO findGeneralInBoard(PlayBoardDTO playBoardDTO, boolean isRed) {
    int fromCol = Default.Game.PlayBoardSize.CENTER_COL_MIN;
    int toCol = Default.Game.PlayBoardSize.CENTER_COL_MAX;
    int fromRow;
    int toRow;

    if (isRed) {
      fromRow = Default.Game.PlayBoardSize.RedArea.CENTER_ROW_MIN;
      toRow = Default.Game.PlayBoardSize.RedArea.CENTER_ROW_MAX;
    } else {
      fromRow = Default.Game.PlayBoardSize.BlackArea.CENTER_ROW_MIN;
      toRow = Default.Game.PlayBoardSize.BlackArea.CENTER_ROW_MAX;
    }

    return findAllInBoard(
      playBoardDTO,
      EPiece.GENERAL.name(),
      null,
      fromCol,
      fromRow,
      toCol,
      toRow
    )
      .stream()
      .findFirst()
      .orElse(null);
  }
}
