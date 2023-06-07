package com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.Default;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.data.entity.MoveHistory;
import com.data.entity.Piece;
import com.data.mapper.PieceMapper;
import com.data.repository.PieceRepository;
import com.service.PlayBoardService;

@Service
public class PlayBoardServiceImpl implements PlayBoardService {
    @Autowired
    private PieceRepository pieceRepository;
    @Autowired
    private PieceMapper pieceMapper;

    @Override
    public PlayBoardDTO create() {
        PlayBoardDTO playBoardDTO = new PlayBoardDTO(
                new PieceDTO[Default.Game.PlayBoardSize.COL][Default.Game.PlayBoardSize.ROW]);
        List<Piece> pieces = pieceRepository.findAll();
        for (Piece piece : pieces) {
            playBoardDTO.getState()[piece.getCurrentCol() - 1][piece.getCurrentRow() - 1] = pieceMapper.toDTO(piece);
        }
        return playBoardDTO;
    }

    @Override
    public PlayBoardDTO update(PlayBoardDTO currentBoard, MoveHistory moveHistory) {
        PieceDTO pieceDTO = pieceMapper.toDTO(moveHistory.getPiece());
        pieceDTO.setCurrentCol(moveHistory.getToCol());
        pieceDTO.setCurrentRow(moveHistory.getToRow());

        currentBoard.getState()[moveHistory.getFromCol() - 1][moveHistory.getFromRow() - 1] = null;
        currentBoard.getState()[moveHistory.getToCol() - 1][moveHistory.getToRow() - 1] = pieceDTO;
        return currentBoard;
    }

}
