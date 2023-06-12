package com.service;

import java.util.List;

import com.common.enumeration.EPiece;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;

public interface PieceService {
    List<PieceDTO> findAll();

    PieceDTO findById(int id);

    EPiece convertByName(String name);

    List<PieceDTO> findAllInBoard(PlayBoardDTO playBoardDTO);

    PieceDTO findOneInBoard(PlayBoardDTO playBoardDTO, int id);

    PieceDTO findLastAtPosition(long matchId, long turn, int toCol, int toRow);

    List<PieceDTO> findAllDeadByMatchId(long matchId);

    List<PieceDTO> findAllDeadInPlayBoard(PlayBoardDTO playBoardDTO);

}
