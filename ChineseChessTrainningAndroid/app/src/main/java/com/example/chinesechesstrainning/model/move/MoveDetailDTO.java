package com.example.chinesechesstrainning.model.move;

import org.jetbrains.annotations.NotNull;

public class MoveDetailDTO {

    private Integer pieceId;

    private Integer toCol;

    private Integer toRow;

    public MoveDetailDTO() {
    }

    public MoveDetailDTO(Integer pieceId, Integer toCol, Integer toRow) {
        this.pieceId = pieceId;
        this.toCol = toCol;
        this.toRow = toRow;
    }

    public Integer getPieceId() {
        return pieceId;
    }

    public void setPieceId(Integer pieceId) {
        this.pieceId = pieceId;
    }

    public Integer getToCol() {
        return toCol;
    }

    public void setToCol(Integer toCol) {
        this.toCol = toCol;
    }

    public Integer getToRow() {
        return toRow;
    }

    public void setToRow(Integer toRow) {
        this.toRow = toRow;
    }
}