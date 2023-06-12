package com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.Default;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.data.entity.Piece;
import com.data.mapper.PieceMapper;
import com.data.repository.MoveHistoryRepository;
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
    @Autowired
    private MoveHistoryRepository moveHistoryRepository;

    @Override
    public PlayBoardDTO create() {
        PieceDTO[][] state = new PieceDTO[MAX_COL][MAX_ROW];
        PlayBoardDTO playBoardDTO = new PlayBoardDTO(state);
        List<Piece> pieces = pieceRepository.findAll();

        for (Piece piece : pieces) {
            PieceDTO pieceDTO = new PieceDTO(pieceMapper.toDTO(piece));
            playBoardDTO.getState()[pieceDTO.getCurrentCol() - 1][pieceDTO.getCurrentRow() - 1] = pieceDTO;
        }

        return playBoardDTO;
    }

    public PlayBoardDTO update(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, int toCol, int toRow) {
        // Create a deep copy of the playBoard
        PlayBoardDTO updateBoard = new PlayBoardDTO(this.deepCopyState(playBoardDTO.getState()));

        // Perform modifications on the updatedBoard
        updateBoard.getState()[pieceDTO.getCurrentCol() - 1][pieceDTO.getCurrentRow() - 1] = null;
        updateBoard.getState()[toCol - 1][toRow - 1] = pieceDTO;

        pieceDTO.setCurrentCol(toCol);
        pieceDTO.setCurrentRow(toRow);

        return updateBoard;
    }

    @Override
    public PlayBoardDTO buildByMatchId(long matchId) {

        return moveHistoryRepository.findAllByMatch_Id(matchId).stream()
                .reduce(create(), (board, mh) -> update(
                        board, pieceMapper.toDTO(mh.getPiece()), mh.getToCol(), mh.getFromRow()),
                        (board1, board2) -> board2);
    }

    private PieceDTO[][] deepCopyState(PieceDTO[][] state) {
        PieceDTO[][] clonedState = new PieceDTO[MAX_COL][MAX_ROW];

        for (int row = 0; row < MAX_COL; row++) {
            for (int col = 0; col < MAX_ROW; col++) {
                PieceDTO pieceDTO = state[row][col];
                if (pieceDTO != null) {
                    clonedState[row][col] = new PieceDTO(pieceDTO);
                }
            }
        }

        return clonedState;
    }

}
