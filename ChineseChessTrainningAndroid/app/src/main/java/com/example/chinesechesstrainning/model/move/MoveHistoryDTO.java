package com.example.chinesechesstrainning.model.move;

import com.example.chinesechesstrainning.model.PieceDTO;
import com.example.chinesechesstrainning.model.PlayBoardDTO;

import java.util.List;

public class MoveHistoryDTO extends MoveCreationResponseDTO {

    private long turn;

    private String description;

    private List<PieceDTO> lastDeadPieceDTOs;

    public MoveHistoryDTO() {
        super();
    }

    public MoveHistoryDTO(PieceDTO movingPieceDTO, int toCol, int toRow, PlayBoardDTO currentBoardDTO, PieceDTO deadPieceDTO, PieceDTO checkedGeneralPieceDTO, boolean isCheckMate, long turn, String description, List<PieceDTO> lastDeadPieceDTOs) {
        super(movingPieceDTO, toCol, toRow, currentBoardDTO, deadPieceDTO, checkedGeneralPieceDTO, isCheckMate);
        this.turn = turn;
        this.description = description;
        this.lastDeadPieceDTOs = lastDeadPieceDTOs;
    }

    public MoveHistoryDTO(long turn, String description, List<PieceDTO> lastDeadPieceDTOs, MoveCreationResponseDTO moveCreationResponseDTO) {
        super(moveCreationResponseDTO.getMovedPieceDTO(), moveCreationResponseDTO.getToCol(), moveCreationResponseDTO.getToRow(),
                moveCreationResponseDTO.getCurrentBoardDTO(), moveCreationResponseDTO.getDeadPieceDTO(), moveCreationResponseDTO.getCheckedGeneralPieceDTO(), moveCreationResponseDTO.isCheckMate());
        this.turn = turn;
        this.description = description;
        this.lastDeadPieceDTOs = lastDeadPieceDTOs;
    }

    public long getTurn() {
        return turn;
    }

    public void setTurn(long turn) {
        this.turn = turn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PieceDTO> getLastDeadPieceDTOs() {
        return lastDeadPieceDTOs;
    }

    public void setLastDeadPieceDTOs(List<PieceDTO> lastDeadPieceDTOs) {
        this.lastDeadPieceDTOs = lastDeadPieceDTOs;
    }
}
