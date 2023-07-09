package com.service;

import java.util.List;

import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.data.entity.MoveHistory;

public interface PlayBoardService {
    
    PlayBoardDTO generate();
    
    PlayBoardDTO buildPlayBoardByMoveHistories(List<MoveHistory> moveHistories);

    PlayBoardDTO update(PlayBoardDTO currentBoard, PieceDTO movingPieceDTO, int toCol, int toRow);


}
