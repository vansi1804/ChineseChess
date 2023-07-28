package com.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.enumeration.EPiece;
import com.config.exception.InvalidException;
import com.config.exception.ResourceNotFoundException;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.data.entity.MoveHistory;
import com.data.entity.Piece;
import com.data.mapper.PieceMapper;
import com.data.repository.PieceRepository;
import com.service.PlayBoardService;
import com.service.MoveRuleService;
import com.service.PieceService;
import com.common.ErrorMessage;
import com.common.Validation;

@Service
public class PlayBoardServiceImpl implements PlayBoardService {

    private final int MAX_COL = Validation.COL_MAX;
    private final int MAX_ROW = Validation.ROW_MAX;

    private final PieceRepository pieceRepository;
    private final PieceMapper pieceMapper;
    private final PieceService pieceService;
    private final MoveRuleService moveRuleService;

    @Autowired
    public PlayBoardServiceImpl(
            PieceRepository pieceRepository,
            PieceMapper pieceMapper,
            PieceService pieceService,
            MoveRuleService moveRuleService) {

        this.pieceRepository = pieceRepository;
        this.pieceMapper = pieceMapper;
        this.pieceService = pieceService;
        this.moveRuleService = moveRuleService;
    }

    @Override
    public PlayBoardDTO generate() {
        PieceDTO[][] state = new PieceDTO[MAX_COL][MAX_ROW];
        PlayBoardDTO playBoardDTO = new PlayBoardDTO(state);
        List<Piece> pieces = pieceRepository.findAll();

        pieces.stream()
                .map(p -> pieceMapper.toDTO(p))
                .forEach(pDTO -> playBoardDTO.getState()[pDTO.getCurrentCol()][pDTO.getCurrentRow()] = pDTO);

        return playBoardDTO;
    }

    @Override
    public PlayBoardDTO buildPlayBoardByMoveHistories(List<MoveHistory> moveHistories) {
        return moveHistories.stream()
                .reduce(
                        generate(),
                        (playBoardDTO, mh) -> update(
                                playBoardDTO, pieceService.findOneInBoard(playBoardDTO, mh.getPiece().getId()),
                                mh.getToCol(), mh.getToRow()),
                        (playBoardDTO, updatedBoardDTO) -> updatedBoardDTO);
    }

    @Override
    public PlayBoardDTO update(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, int toCol, int toRow) {
        PlayBoardDTO updatePlayBoardDTO = new PlayBoardDTO(cloneStateArray(playBoardDTO.getState()));

        updatePlayBoardDTO.getState()[pieceDTO.getCurrentCol()][pieceDTO.getCurrentRow()] = null;

        PieceDTO updatedPieceDTO = pieceMapper.copy(pieceDTO);
        updatedPieceDTO.setCurrentCol(toCol);
        updatedPieceDTO.setCurrentRow(toRow);

        updatePlayBoardDTO.getState()[toCol][toRow] = updatedPieceDTO;

        return updatePlayBoardDTO;
    }

    @Override
    public void validatePlayBoard(PlayBoardDTO playBoardDTO) {
        int colLength = playBoardDTO.getState().length;
        int rowLength = playBoardDTO.getState()[0].length;

        if (MAX_COL != colLength || MAX_ROW != rowLength) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("colLength", ErrorMessage.COL_LENGTH);
            errors.put("rowLength", ErrorMessage.ROW_LENGTH);

            throw new InvalidException(ErrorMessage.PLAY_BOARD_SIZE, errors);
        } else {
            boolean existsRedGeneral = false;
            boolean existsBlackGeneral = false;

            for (int col = 0; col < playBoardDTO.getState().length; col++) {
                for (int row = 0; row < playBoardDTO.getState()[0].length; row++) {
                    PieceDTO pieceDTO = playBoardDTO.getState()[col][row];
                    if (pieceDTO != null) {
                        // validate id
                        if (!pieceRepository.existsById(pieceDTO.getId())) {
                            throw new ResourceNotFoundException(buildValidateErrors(pieceDTO, col, row));
                        }

                        // validate name
                        EPiece ePiece = pieceService.convertByName(pieceDTO.getName());

                        // check exists generals
                        if (!existsRedGeneral || !existsBlackGeneral) {
                            if (EPiece.GENERAL == ePiece) {
                                if (pieceDTO.isRed()) {
                                    existsRedGeneral = true;
                                } else {
                                    existsBlackGeneral = true;
                                }
                            }
                        }

                        // validate index
                        if (col != pieceDTO.getCurrentCol() || row != pieceDTO.getCurrentRow()) {
                            throw new InvalidException(buildValidateErrors(pieceDTO, col, row));
                        }
                    }
                }
            }

            if (!existsRedGeneral) {
                throw new InvalidException(Collections.singletonMap("message", "Red general is not found in board"));
            }

            if (!existsBlackGeneral) {
                throw new InvalidException(Collections.singletonMap("message", "Black general is not found in board"));
            }
        }
    }

    private Map<String, Object> buildValidateErrors(PieceDTO pieceDTO, int col, int row) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("pieceDTO", pieceDTO);
        errors.put("col", col);
        errors.put("row", row);

        return errors;
    }

    private PieceDTO[][] cloneStateArray(PieceDTO[][] state) {
        return Arrays.stream(state)
                .map(PieceDTO[]::clone)
                .toArray(PieceDTO[][]::new);
    }

    @Override
    public void printTest(Object title, PlayBoardDTO playBoardDTO, PieceDTO pieceDTO) {
        System.out.println("\n===========================================");
        System.out.println(title.toString());
        System.out.println("===========================================");

        for (int row = 0; row < playBoardDTO.getState()[0].length; row++) {
            for (int col = 0; col < playBoardDTO.getState().length; col++) {
                PieceDTO indexPieceDTO = playBoardDTO.getState()[col][row];
                System.out.print(getSymbolOutput(pieceDTO, col, row, indexPieceDTO, false));
            }
            System.out.println("\n\n");
        }

        System.out.println("===========================================");
    }

    @Override
    public void printTest(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, List<int[]> availableMoveIndexes) {
        System.out.println("\n===========================================");
        System.out.println("Available move: ");
        System.out.println("===========================================");

        for (int row = 0; row < playBoardDTO.getState()[0].length; row++) {
            for (int col = 0; col < playBoardDTO.getState().length; col++) {
                PieceDTO indexPieceDTO = playBoardDTO.getState()[col][row];
                int[] index = new int[] { col, row };
                boolean containsIndex = availableMoveIndexes.stream()
                        .anyMatch(arr -> Arrays.equals(arr, index));
                if (containsIndex) {
                    System.out.print(getSymbolOutput(null, col, row, indexPieceDTO, true));
                } else {
                    System.out.print(getSymbolOutput(pieceDTO, col, row, indexPieceDTO, false));
                }
            }
            System.out.println("\n\n\n");
        }

        System.out.println("===========================================");
    }

    private String getSymbolOutput(PieceDTO pieceDTO, int col, int row, PieceDTO indexPieceDTO,
            boolean isValidMoveFinding) {
        if (indexPieceDTO == null) {
            if ((pieceDTO != null) && ((col == pieceDTO.getCurrentCol()) && (row == pieceDTO.getCurrentRow()))) {
                return "   [ ]   ";
            } else if (isValidMoveFinding) {
                return "    O    ";
            } else {
                return "    +    ";
            }
        } else {
            String symbolId = String.valueOf(indexPieceDTO.getId());
            symbolId = symbolId.length() == 1 ? "0" + symbolId : symbolId;

            String p = pieceService.convertByName(indexPieceDTO.getName()).getShortName();
            p = (p.length() == 1 ? " " + p : p)
                    + (indexPieceDTO.isRed() ? "1" : "2")
                    + "_"
                    + (p.length() == 1 ? symbolId + " " : symbolId);

            if ((pieceDTO != null) && (indexPieceDTO.getId() == pieceDTO.getId())) {
                return "[" + p + "]";
            } else if (isValidMoveFinding) {
                return "(" + p + ")";
            } else {
                return " " + p + " ";
            }
        }
    }

    @Override
    public boolean areTwoGeneralsFacing(PlayBoardDTO playBoardDTO, PieceDTO generalPiece1, PieceDTO generalPiece2) {
        if (generalPiece1.getCurrentCol() == generalPiece2.getCurrentCol()) {
            int currentCol = generalPiece1.getCurrentCol();
            int fromRow = generalPiece1.getCurrentRow();
            int toRow = generalPiece2.getCurrentRow();

            if (!pieceService.existsBetweenInColPath(playBoardDTO, currentCol, fromRow, toRow)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isGeneralBeingChecked(PlayBoardDTO playBoardDTO, PieceDTO generalPieceDTO) {
        List<PieceDTO> opponentPieceDTOsInBoard = pieceService.findAllInBoard(
                playBoardDTO, null, !generalPieceDTO.isRed());

        return opponentPieceDTOsInBoard.stream()
                .anyMatch(opponentPiece -> moveRuleService.isValidMove(
                        playBoardDTO, opponentPiece, generalPieceDTO.getCurrentCol(), generalPieceDTO.getCurrentRow()));
    }

    @Override
    public boolean isGeneralInSafe(PlayBoardDTO playBoardDTO, boolean isRed) {
        PieceDTO sameColorGeneral = pieceService.findGeneralInBoard(playBoardDTO, isRed);
        PieceDTO opponentGeneral = pieceService.findGeneralInBoard(playBoardDTO, !isRed);

        return !areTwoGeneralsFacing(playBoardDTO, sameColorGeneral, opponentGeneral)
                && !isGeneralBeingChecked(playBoardDTO, sameColorGeneral);
    }

}
