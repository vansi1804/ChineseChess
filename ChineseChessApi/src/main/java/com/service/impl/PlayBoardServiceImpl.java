package com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.Default;
import com.data.dto.PieceDTO;
import com.data.entity.MoveHistory;
import com.data.entity.Piece;
import com.data.mapper.PieceMapper;
import com.data.repository.MoveHistoryRepository;
import com.data.repository.PieceRepository;
import com.service.PlayBoardService;

@Service
public class PlayBoardServiceImpl implements PlayBoardService {
    @Autowired
    private PieceRepository pieceRepository;
    @Autowired
    private PieceMapper pieceMapper;
    @Autowired
    private MoveHistoryRepository moveHistoryRepository;

    @Override
    public PieceDTO[][] create() {
        PieceDTO[][] board = new PieceDTO[Default.COL][Default.ROW];
        List<Piece> pieces = pieceRepository.findAll();
        for (Piece piece : pieces) {
            board[piece.getCurrentCol() - 1][piece.getCurrentRow() - 1] = pieceMapper.toDTO(piece);
        }
        return board;
    }

    @Override
    public PieceDTO[][] update(PieceDTO[][] currentBoard, MoveHistory moveHistory) {
        moveHistoryRepository.save(moveHistory);
        currentBoard[moveHistory.getFromCol() - 1][moveHistory.getFromRow() - 1] = null;
        moveHistory.getPiece().setCurrentCol(moveHistory.getToCol());
        moveHistory.getPiece().setCurrentRow(moveHistory.getToRow());
        currentBoard[moveHistory.getToCol() - 1][moveHistory.getToRow() - 1] = pieceMapper.toDTO(moveHistory.getPiece());
        return currentBoard;
    }

}
