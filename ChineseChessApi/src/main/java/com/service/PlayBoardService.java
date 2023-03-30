package com.service;

import com.data.dto.PlayBoardDTO;
import com.data.entity.MoveHistory;

public interface PlayBoardService {
    PlayBoardDTO create();
    
    PlayBoardDTO update(PlayBoardDTO currentBoard, MoveHistory moveHistory);
}
