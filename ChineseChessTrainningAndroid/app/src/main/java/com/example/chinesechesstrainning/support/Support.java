package com.example.chinesechesstrainning.support;

import com.example.chinesechesstrainning.model.PieceDTO;
import com.example.chinesechesstrainning.model.PlayBoardDTO;
import com.example.chinesechesstrainning.model.move.MoveHistoryDTO;
import com.example.chinesechesstrainning.model.move.TrainingMoveCreationDTO;
import com.example.chinesechesstrainning.model.training.TrainingDTO;
import com.example.chinesechesstrainning.model.training.TrainingDetailDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Support {
    private static final int COL = 9;
    private static final int ROW = 10;

    public static PieceDTO findPieceInBoard(int pieceId, PlayBoardDTO playBoardDTO) {
        for (int col = 0; col <= COL - 1; col++) {
            for (int row = 0; row <= ROW - 1; row++) {
                if (playBoardDTO.getState()[col][row] != null && playBoardDTO.getState()[col][row].getId() == pieceId) {
                    return playBoardDTO.getState()[col][row];
                }
            }
        }
        return null;
    }

    public static List<PieceDTO> findAllPieceInBoard(PlayBoardDTO playBoardDTO) {
        List<PieceDTO> pieceDTOSInBoard = new ArrayList<>();
        for (int col = 0; col <= COL - 1; col++) {
            for (int row = 0; row <= ROW - 1; row++) {
                PieceDTO pieceDTO = playBoardDTO.getState()[col][row];
                if (pieceDTO != null) {
                    pieceDTOSInBoard.add(pieceDTO);
                }
            }
        }
        return pieceDTOSInBoard;
    }

    public static List<PieceDTO> findAllDeadPieceInBoard(PlayBoardDTO playBoardDTO) {
        List<PieceDTO> deadPieceDTOs = DataTest.pieceData();
        List<PieceDTO> pieceDTOSInBoard = findAllPieceInBoard(playBoardDTO);

        deadPieceDTOs.removeIf(
                deadPiece -> pieceDTOSInBoard.stream()
                        .map(PieceDTO::getId)
                        .collect(Collectors.toList())
                        .contains(deadPiece.getId()));

        return deadPieceDTOs;
    }

    public static PlayBoardDTO updatePlayBoard(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, int toCol, int toRow) {
        PlayBoardDTO updatePlayBoardDTO = new PlayBoardDTO(cloneStateArray(playBoardDTO.getState()));

        updatePlayBoardDTO.getState()[pieceDTO.getCurrentCol()][pieceDTO.getCurrentRow()] = null;

        PieceDTO updatedPieceDTO = new PieceDTO(pieceDTO);
        updatedPieceDTO.setCurrentCol(toCol);
        updatedPieceDTO.setCurrentRow(toRow);

        updatePlayBoardDTO.getState()[toCol][toRow] = updatedPieceDTO;

        return updatePlayBoardDTO;
    }

    private static PieceDTO[][] cloneStateArray(PieceDTO[][] state) {
        return Arrays.stream(state)
                .map(PieceDTO[]::clone)
                .toArray(PieceDTO[][]::new);
    }

    public static TrainingDTO findTrainingById(Long id) {
        return DataTest.trainingData().stream()
                .filter(t -> t.getId().equals(id))
                .peek(t -> t.setChildTrainings(findAllChildrenTrainingByParentTrainingId(t.getId())))
                .findFirst()
                .orElse(null);
    }

    public static List<TrainingDTO> findAllChildrenTrainingByParentTrainingId(Long parentTrainingId) {
        return DataTest.trainingData().stream()
                .filter(t -> Objects.equals(t.getParentTrainingId(), parentTrainingId))
                .collect(Collectors.toList());
    }


    public static MoveHistoryDTO buildMoveHistory(AtomicReference<PlayBoardDTO> currentBoardDTO, int pieceId, int toCol, int toRow) {
        PieceDTO movingPieceDTO = Support.findPieceInBoard(pieceId, currentBoardDTO.get());
        if (movingPieceDTO == null) {
            throw new IllegalStateException("Piece with id = " + pieceId + "is dead");
        }

        String description = movingPieceDTO.getName()
                + ": [" + movingPieceDTO.getCurrentCol() + ";" + movingPieceDTO.getCurrentRow() + "]"
                + "->"
                + "[" + toCol + ";" + toRow + "]";

        List<PieceDTO> lastDeadPieceDTOs = findAllDeadPieceInBoard(currentBoardDTO.get());

        PieceDTO deadPieceDTO = currentBoardDTO.get().getState()[toCol][toRow];

        PlayBoardDTO updatedPlayBoardDTO = updatePlayBoard(currentBoardDTO.get(), movingPieceDTO, toCol, toRow);
        currentBoardDTO.set(updatedPlayBoardDTO);

        PieceDTO checkedGeneralPieceDTO = null;

        boolean isCheckMate = false;

        return new MoveHistoryDTO(movingPieceDTO, toCol, toRow, currentBoardDTO.get(), deadPieceDTO, checkedGeneralPieceDTO, isCheckMate, 0, description, lastDeadPieceDTOs);
    }

    public static TrainingDetailDTO findTrainingDetailById(long id) {
        return DataTest.trainingDetailData().stream()
                .filter(td -> Objects.equals(td.getTrainingDTO().getId(), id))
                .findFirst()
                .orElse(new TrainingDetailDTO(findTrainingById(id), 0, new HashMap<>()));
    }

    public static PlayBoardDTO generatePlayBoard() {
        PlayBoardDTO playBoardDTO = new PlayBoardDTO();

        DataTest.pieceData().forEach(piece -> playBoardDTO.getState()[piece.getCurrentCol()][piece.getCurrentRow()] = new PieceDTO(piece));

        return playBoardDTO;
    }

    public static TrainingDetailDTO buildTrainingDetails
            (List<TrainingMoveCreationDTO> trainingMoveCreationDTOs) {
        AtomicReference<PlayBoardDTO> currentBoard = new AtomicReference<>(generatePlayBoard());

        TrainingDetailDTO trainingDetailDTO = new TrainingDetailDTO();

        trainingDetailDTO.setTrainingDTO(findTrainingById(trainingMoveCreationDTOs.get(0).getTrainingId()));

        Map<Long, MoveHistoryDTO> moveHistoryDTOs = new HashMap<>();
        long turn = 1;
        for (TrainingMoveCreationDTO trainingMoveCreationDTO : trainingMoveCreationDTOs) {
            MoveHistoryDTO moveHistoryDTO  = buildMoveHistory(currentBoard, trainingMoveCreationDTO.getPieceId(), trainingMoveCreationDTO.getToCol(), trainingMoveCreationDTO.getToRow());
            moveHistoryDTO.setTurn(turn);
            moveHistoryDTOs.put(moveHistoryDTO.getTurn(),moveHistoryDTO);
            turn++;
        }
        trainingDetailDTO.setMoveHistoryDTOs(moveHistoryDTOs);

        trainingDetailDTO.setTotalTurn(moveHistoryDTOs.size());

        return trainingDetailDTO;
    }
}
