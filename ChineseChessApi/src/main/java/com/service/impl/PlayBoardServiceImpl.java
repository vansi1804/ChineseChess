package com.service.impl;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.data.entity.MoveHistory;
import com.data.entity.Piece;
import com.data.mapper.PieceMapper;
import com.data.repository.PieceRepository;
import com.service.PlayBoardService;
import com.service.MoveRuleService;
import com.service.PieceService;
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
    public PlayBoardDTO build(List<MoveHistory> moveHistories) {
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
    public boolean areTwoGeneralsFacing(PlayBoardDTO playBoardDTO, PieceDTO generalPieceDTO1, PieceDTO generalPieceDTO2) {
        if (generalPieceDTO1.getCurrentCol() == generalPieceDTO2.getCurrentCol()) {
            int currentCol = generalPieceDTO1.getCurrentCol();
            int fromRow = generalPieceDTO1.getCurrentRow();
            int toRow = generalPieceDTO2.getCurrentRow();

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
                .anyMatch(opponentPiece -> moveRuleService.isValid(
                        playBoardDTO, opponentPiece, generalPieceDTO.getCurrentCol(), generalPieceDTO.getCurrentRow()));
    }

    @Override
    public boolean isGeneralInSafe(PlayBoardDTO playBoardDTO, PieceDTO generalPieceDTO) {
        PieceDTO opponentGeneralDTO = pieceService.findGeneralInBoard(playBoardDTO, !generalPieceDTO.isRed());

        return !areTwoGeneralsFacing(playBoardDTO, generalPieceDTO, opponentGeneralDTO)
                && !isGeneralBeingChecked(playBoardDTO, generalPieceDTO);
    }
 
    private PieceDTO[][] cloneStateArray(PieceDTO[][] state) {
        return Arrays.stream(state)
                .map(PieceDTO[]::clone)
                .toArray(PieceDTO[][]::new);
    }

}
