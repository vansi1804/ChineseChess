package com.service;

import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;

public interface PlayBoardService {
    PlayBoardDTO create();

    PlayBoardDTO update(PlayBoardDTO currentBoard, PieceDTO movingPieceDTO, int toCol, int toRow);

}
