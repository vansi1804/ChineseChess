package com.service;

import java.util.List;

import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.data.entity.MoveHistory;

public interface PlayBoardService {
    
    PlayBoardDTO generate();
    
    PlayBoardDTO buildPlayBoardByMoveHistories(List<MoveHistory> moveHistories);

    PlayBoardDTO update(PlayBoardDTO playBoardDTO, PieceDTO movingPieceDTO, int toCol, int toRow);
   
    void validatePlayBoard(PlayBoardDTO playBoardDTO);
    
    void printTest(Object title, PlayBoardDTO playBoardDTO, PieceDTO pieceDTO);

    void printTest(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, List<int[]> availableMoveIndexes);

    boolean isGeneralInSafe(PlayBoardDTO playBoardDTO, boolean isRed);

    boolean areTwoGeneralsFacing(PlayBoardDTO playBoardDTO, PieceDTO generalPiece1, PieceDTO generalPiece2);

    boolean isGeneralBeingChecked(PlayBoardDTO playBoardDTO, PieceDTO generalPieceDTO);

}
