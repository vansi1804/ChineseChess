package com.example.chinesechesstrainning.model.move;

import com.example.chinesechesstrainning.model.PieceDTO;
import com.example.chinesechesstrainning.model.PlayBoardDTO;

public class MoveCreationResponseDTO {

    private PieceDTO movedPieceDTO;

    private int toCol;

    private int toRow;

    private PlayBoardDTO currentBoardDTO;

    private PieceDTO deadPieceDTO;

    private PieceDTO checkedGeneralPieceDTO;

    private boolean isCheckMate;

    public MoveCreationResponseDTO() {
    }

    public MoveCreationResponseDTO(PieceDTO movedPieceDTO, int toCol, int toRow, PlayBoardDTO currentBoardDTO, PieceDTO deadPieceDTO, PieceDTO checkedGeneralPieceDTO, boolean isCheckMate) {
        this.movedPieceDTO = movedPieceDTO;
        this.toCol = toCol;
        this.toRow = toRow;
        this.currentBoardDTO = currentBoardDTO;
        this.deadPieceDTO = deadPieceDTO;
        this.checkedGeneralPieceDTO = checkedGeneralPieceDTO;
        this.isCheckMate = isCheckMate;
    }

    public PieceDTO getMovedPieceDTO() {
        return movedPieceDTO;
    }

    public void setMovedPieceDTO(PieceDTO movedPieceDTO) {
        this.movedPieceDTO = movedPieceDTO;
    }

    public int getToCol() {
        return toCol;
    }

    public void setToCol(int toCol) {
        this.toCol = toCol;
    }

    public int getToRow() {
        return toRow;
    }

    public void setToRow(int toRow) {
        this.toRow = toRow;
    }

    public PlayBoardDTO getCurrentBoardDTO() {
        return currentBoardDTO;
    }

    public void setCurrentBoardDTO(PlayBoardDTO currentBoardDTO) {
        this.currentBoardDTO = currentBoardDTO;
    }

    public PieceDTO getDeadPieceDTO() {
        return deadPieceDTO;
    }

    public void setDeadPieceDTO(PieceDTO deadPieceDTO) {
        this.deadPieceDTO = deadPieceDTO;
    }

    public PieceDTO getCheckedGeneralPieceDTO() {
        return checkedGeneralPieceDTO;
    }

    public void setCheckedGeneralPieceDTO(PieceDTO checkedGeneralPieceDTO) {
        this.checkedGeneralPieceDTO = checkedGeneralPieceDTO;
    }

    public boolean isCheckMate() {
        return isCheckMate;
    }

    public void setCheckMate(boolean checkMate) {
        isCheckMate = checkMate;
    }
}
