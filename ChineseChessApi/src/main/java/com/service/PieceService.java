package com.service;

import java.util.List;

import com.common.enumeration.EPiece;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;

public interface PieceService {
    
    List<PieceDTO> findAll();

    PieceDTO findById(int id);

    EPiece convertByName(String name);

    List<PieceDTO> findAllInBoard(PlayBoardDTO playBoardDTO, String name, Boolean isRed);

    PieceDTO findOneInBoard(PlayBoardDTO playBoardDTO, int id);

    PieceDTO findLastAtPosition(long matchId, long turn, int toCol, int toRow);

    List<PieceDTO> findAllDeadInPlayBoard(PlayBoardDTO playBoardDTO);

    PieceDTO findExistingTheSameInColPath(PlayBoardDTO playBoard, PieceDTO pieceDTO);

    boolean existsBetweenInRowPath(PlayBoardDTO playBoard, int currentRow, int fromCol, int toCol);

    boolean existsBetweenInColPath(PlayBoardDTO playBoard, int currentCol, int fromRow, int toRow);

    int countBetweenInRowPath(PlayBoardDTO playBoard, int currentRow, int fromCol, int toCol);

    int countBetweenInColPath(PlayBoardDTO playBoard, int currentCol, int fromRow, int toRow);

}
