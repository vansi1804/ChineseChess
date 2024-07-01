package com.nvs.service;

import com.nvs.data.dto.PieceDTO;
import com.nvs.data.dto.PlayBoardDTO;

public interface MoveDescriptionService{

   String build(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, int toCol, int toRow);

}
