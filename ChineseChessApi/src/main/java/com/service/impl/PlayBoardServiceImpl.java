package com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.Default;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.data.entity.Piece;
import com.data.mapper.PieceMapper;
import com.data.repository.PieceRepository;
import com.service.PlayBoardService;

@Service
public class PlayBoardServiceImpl implements PlayBoardService {
    private final int MAX_COL = Default.Game.PlayBoardSize.COL;
    private final int MAX_ROW = Default.Game.PlayBoardSize.ROW;

    @Autowired
    private PieceRepository pieceRepository;
    @Autowired
    private PieceMapper pieceMapper;

    @Override
    public PlayBoardDTO create() {
        PieceDTO[][] state = new PieceDTO[MAX_COL][MAX_ROW];
        PlayBoardDTO playBoardDTO = new PlayBoardDTO(state);
        List<Piece> pieces = pieceRepository.findAll();

        for (Piece piece : pieces) {
            playBoardDTO.getState()[piece.getCurrentCol() - 1][piece.getCurrentRow() - 1] = pieceMapper.toDTO(piece);
        }

        return playBoardDTO;
    }

    @Override
    public PlayBoardDTO update(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, int toCol, int toRow) {
        PlayBoardDTO updatedBoard = new PlayBoardDTO();
        PieceDTO[][] state = playBoardDTO.getState();
        PieceDTO[][] updatedState = copyStateArray(state); // Use the copyStateArray function
        updatedBoard.setState(updatedState);

        // Update the piece's current position in the updatedBoard
        updatedBoard.getState()[pieceDTO.getCurrentCol() - 1][pieceDTO.getCurrentRow() - 1] = null;
        PieceDTO updatedPieceDTO = new PieceDTO(pieceDTO);
        updatedPieceDTO.setCurrentCol(toCol);
        updatedPieceDTO.setCurrentRow(toRow);
        updatedBoard.getState()[toCol - 1][toRow - 1] = updatedPieceDTO;

        return updatedBoard;
    }

    private PieceDTO[][] copyStateArray(PieceDTO[][] state) {
        PieceDTO[][] copiedState = new PieceDTO[state.length][];
        for (int i = 0; i < state.length; i++) {
            copiedState[i] = state[i].clone();
        }
        return copiedState;
    }

}
