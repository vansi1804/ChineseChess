package com.service;

import com.common.enumeration.EPiece;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import java.util.List;

public interface PieceService {
  List<PieceDTO> findAll();

  PieceDTO findById(int id);

  EPiece convertByName(String name);

  List<PieceDTO> findAllInBoard(
    PlayBoardDTO playBoardDTO,
    String name,
    Boolean isRed
  );

  List<PieceDTO> findAllInBoard(    PlayBoardDTO playBoardDTO,
    String name,
    Boolean isRed,
    int fromCol,
    int fromRow,
    int toCol,
    int toRow
  );

  PieceDTO findOneInBoard(PlayBoardDTO playBoardDTO, int id);

  List<PieceDTO> findAllNotInPlayBoard(PlayBoardDTO playBoardDTO);

  PieceDTO findExistingTheSameInColPath(
    PlayBoardDTO playBoardDTO,
    PieceDTO pieceDTO
  );

  boolean existsBetweenInRowPath(
    PlayBoardDTO playBoardDTO,
    int row,
    int fromCol,
    int toCol
  );

  boolean existsBetweenInColPath(
    PlayBoardDTO playBoardDTO,
    int col,
    int fromRow,
    int toRow
  );

  int countBetweenInRowPath(
    PlayBoardDTO playBoardDTO,
    int row,
    int fromCol,
    int toCol
  );

  int countBetweenInColPath(
    PlayBoardDTO playBoardDTO,
    int col,
    int fromRow,
    int toRow
  );

  PieceDTO findGeneralInBoard(PlayBoardDTO playBoardDTO, boolean isRed);
}
