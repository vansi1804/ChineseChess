package com.nvs.service.impl;

import com.nvs.common.Default;
import com.nvs.common.enumeration.EPiece;
import com.nvs.config.exception.ResourceNotFoundExceptionCustomize;
import com.nvs.data.dto.PieceDTO;
import com.nvs.data.dto.PlayBoardDTO;
import com.nvs.data.mapper.PieceMapper;
import com.nvs.data.repository.PieceRepository;
import com.nvs.service.PieceService;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PieceServiceImpl implements PieceService {

  private final PieceRepository pieceRepository;
  private final PieceMapper pieceMapper;

  @Override
  @Cacheable(value = "pieces")
  public List<PieceDTO> findAll() {
    log.debug("-- Fetching all pieces from repository.");
    return pieceRepository.findAll().stream()
        .map(pieceMapper::toDTO)
        .collect(Collectors.toList());
  }

  @Override
  public PieceDTO findById(int id) {
    log.debug("-- Finding piece by id: {}", id);
    return pieceRepository.findById(id)
        .map(pieceMapper::toDTO)
        .orElseThrow(() -> {
          log.error("Piece with id {} not found.", id);
          return new ResourceNotFoundExceptionCustomize(Collections.singletonMap("id", id));
        });
  }

  @Override
  public EPiece convertByName(String name) {
    log.debug("-- Converting name to EPiece: {}", name);
    try {
      return EPiece.valueOf(name);
    } catch (IllegalArgumentException | NullPointerException e) {
      log.warn("-- Failed to convert name to EPiece: {}", name, e);
      return null;
    }
  }

  @Override
  public List<PieceDTO> findAllInBoard(PlayBoardDTO playBoardDTO, String name, Boolean isRed,
      int fromCol, int fromRow, int toCol, int toRow) {
    log.debug("-- Finding pieces in board from ({}, {}) to ({}, {}), name: {}, isRed: {}",
        fromCol, fromRow, toCol, toRow, name, isRed);

    List<PieceDTO> pieces = IntStream.rangeClosed(fromCol, toCol).boxed()
        .flatMap(col -> IntStream.rangeClosed(fromRow, toRow).filter(row -> {
          PieceDTO pieceDTO = playBoardDTO.getState()[col][row];
          return ((pieceDTO != null) && (StringUtils.isBlank(name) || pieceDTO.getName()
              .equals(name)) && ((isRed == null) || (pieceDTO.isRed() == isRed)));
        }).mapToObj(row -> playBoardDTO.getState()[col][row]))
        .collect(Collectors.toList());

    log.debug("-- Found {} pieces in the specified range.", pieces.size());
    return pieces;
  }

  @Override
  public List<PieceDTO> findAllInBoard(PlayBoardDTO playBoardDTO, String name, Boolean isRed) {
    log.debug("-- Finding all pieces in board with name: {}, isRed: {}", name, isRed);
    int fromCol = 0;
    int fromRow = 0;
    int toCol = playBoardDTO.getState().length - 1;
    int toRow = playBoardDTO.getState()[0].length - 1;

    return findAllInBoard(playBoardDTO, name, isRed, fromCol, fromRow, toCol, toRow);
  }

  @Override
  public List<PieceDTO> findAllNotInPlayBoard(PlayBoardDTO playBoardDTO) {
    log.debug("-- Finding all pieces not in play board.");
    List<PieceDTO> piecesInBoard = findAllInBoard(playBoardDTO, null, null);
    List<PieceDTO> deadPieces = findAll();

    deadPieces.removeIf(deadPiece -> piecesInBoard.stream()
        .map(PieceDTO::getId)
        .toList()
        .contains(deadPiece.getId()));

    log.debug("-- Found {} pieces not in play board.", deadPieces.size());
    return deadPieces;
  }

  @Override
  public PieceDTO findOneInBoard(PlayBoardDTO playBoardDTO, int id) {
    log.debug("-- Finding one piece by id: {} in board.", id);
    int fromCol = 0;
    int fromRow = 0;
    int toCol = playBoardDTO.getState().length - 1;
    int toRow = playBoardDTO.getState()[0].length - 1;

    PieceDTO piece = IntStream.rangeClosed(fromCol, toCol).boxed()
        .flatMap(col -> IntStream.rangeClosed(fromRow, toRow).filter(row -> {
          PieceDTO pieceDTO = playBoardDTO.getState()[col][row];
          return (pieceDTO != null) && (pieceDTO.getId() == id);
        }).mapToObj(row -> playBoardDTO.getState()[col][row]))
        .findFirst()
        .orElse(null);

    if (piece == null) {
      log.warn("-- Piece with id {} not found in board.", id);
    } else {
      log.debug("-- Found piece with id {}: {}", id, piece);
    }

    return piece;
  }

  @Override
  public PieceDTO findExistingTheSameInColPath(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO) {
    log.debug("-- Finding existing piece with same attributes in column path for piece: {}",
        pieceDTO);
    int col = pieceDTO.getCurrentCol();
    int fromRow = 0;
    int toRow = playBoardDTO.getState()[0].length - 1;

    PieceDTO foundPiece = IntStream.rangeClosed(fromRow, toRow).filter(row -> {
      PieceDTO currentPiece = playBoardDTO.getState()[col][row];
      return ((currentPiece != null) && (!Objects.equals(currentPiece.getId(), pieceDTO.getId()))
          && (currentPiece.isRed() == pieceDTO.isRed()) && currentPiece.getName()
          .equals(pieceDTO.getName()));
    }).mapToObj(row -> playBoardDTO.getState()[col][row]).findFirst().orElse(null);

    if (foundPiece == null) {
      log.debug("-- No existing piece with same attributes found in column path.");
    } else {
      log.debug("-- Found existing piece in column path: {}", foundPiece);
    }

    return foundPiece;
  }

  @Override
  public boolean existsBetweenInRowPath(PlayBoardDTO playBoardDTO, int currentRow, int fromCol,
      int toCol) {
    log.debug("-- Checking if there are any pieces between columns {} and {} in row {}.", fromCol,
        toCol, currentRow);
    int startCol = Math.min(fromCol, toCol) + 1;
    int endCol = Math.max(fromCol, toCol) - 1;

    boolean exists = IntStream.rangeClosed(startCol, endCol)
        .anyMatch(col -> playBoardDTO.getState()[col][currentRow] != null);

    log.debug("-- Existence of pieces between columns {} and {} in row {}: {}", fromCol, toCol,
        currentRow, exists);
    return exists;
  }

  @Override
  public boolean existsBetweenInColPath(PlayBoardDTO playBoardDTO, int currentCol, int fromRow,
      int toRow) {
    log.debug("-- Checking if there are any pieces between rows {} and {} in column {}.", fromRow,
        toRow, currentCol);
    int startRow = Math.min(fromRow, toRow) + 1;
    int endRow = Math.max(fromRow, toRow) - 1;

    boolean exists = IntStream.rangeClosed(startRow, endRow)
        .noneMatch(row -> playBoardDTO.getState()[currentCol][row] != null);

    log.debug("-- Existence of pieces between rows {} and {} in column {}: {}", fromRow, toRow,
        currentCol, exists);
    return exists;
  }

  @Override
  public int countBetweenInRowPath(PlayBoardDTO playBoardDTO, int currentRow, int fromCol,
      int toCol) {
    log.debug("-- Counting pieces between columns {} and {} in row {}.", fromCol, toCol,
        currentRow);
    int startCol = Math.min(fromCol, toCol) + 1;
    int endCol = Math.max(fromCol, toCol) - 1;

    int count = (int) IntStream.rangeClosed(startCol, endCol)
        .filter(col -> playBoardDTO.getState()[col][currentRow] != null)
        .count();

    log.debug("-- Count of pieces between columns {} and {} in row {}: {}", fromCol, toCol,
        currentRow,
        count);
    return count;
  }

  @Override
  public int countBetweenInColPath(PlayBoardDTO playBoardDTO, int currentCol, int fromRow,
      int toRow) {
    log.debug("-- Counting pieces between rows {} and {} in column {}.", fromRow, toRow,
        currentCol);
    int startRow = Math.min(fromRow, toRow) + 1;
    int endRow = Math.max(fromRow, toRow) - 1;

    int count = (int) IntStream.rangeClosed(startRow, endRow)
        .filter(row -> playBoardDTO.getState()[currentCol][row] != null)
        .count();

    log.debug("-- Count of pieces between rows {} and {} in column {}: {}", fromRow, toRow,
        currentCol,
        count);
    return count;
  }

  @Override
  public PieceDTO findGeneralInBoard(PlayBoardDTO playBoardDTO, boolean isRed) {
    log.debug("-- Finding general in board for color: {}", isRed ? "Red" : "Black");
    int fromCol = Default.Game.PlayBoardSize.CENTER_COL_MIN;
    int toCol = Default.Game.PlayBoardSize.CENTER_COL_MAX;
    int fromRow = isRed ? Default.Game.PlayBoardSize.RedArea.CENTER_ROW_MIN
        : Default.Game.PlayBoardSize.BlackArea.CENTER_ROW_MIN;
    int toRow = isRed ? Default.Game.PlayBoardSize.RedArea.CENTER_ROW_MAX
        : Default.Game.PlayBoardSize.BlackArea.CENTER_ROW_MAX;

    PieceDTO general = findAllInBoard(playBoardDTO, EPiece.GENERAL.name(), null, fromCol, fromRow,
        toCol, toRow)
        .stream()
        .findFirst()
        .orElse(null);

    if (general == null) {
      log.debug("-- General not found in board for color: {}", isRed ? "Red" : "Black");
    } else {
      log.debug("-- Found general in board for color: {}: {}", isRed ? "Red" : "Black", general);
    }

    return general;
  }

}
