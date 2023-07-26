package com.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.Default;
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
import com.service.PieceService;

@Service
public class PlayBoardServiceImpl implements PlayBoardService {

    private final int MAX_COL = Default.Game.PlayBoardSize.COL_MAX;
    private final int MAX_ROW = Default.Game.PlayBoardSize.ROW_MAX;

    private final PieceRepository pieceRepository;
    private final PieceMapper pieceMapper;
    private final PieceService pieceService;

    @Autowired
    public PlayBoardServiceImpl(PieceRepository pieceRepository, PieceMapper pieceMapper, PieceService pieceService) {
        this.pieceRepository = pieceRepository;
        this.pieceMapper = pieceMapper;
        this.pieceService = pieceService;
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
        boolean existsRedGeneral = false;
        boolean existsBlackGeneral = false;

        for (int col = 0; col < playBoardDTO.getState().length; col++) {
            for (int row = 0; row < playBoardDTO.getState()[0].length; row++) {
                PieceDTO pieceDTO = playBoardDTO.getState()[col][row];
                if (pieceDTO != null) {
                    if (!pieceRepository.existsById(pieceDTO.getId())) {
                        throw new ResourceNotFoundException(Collections.singletonMap("pieceId", pieceDTO.getId()));
                    }

                    if (!existsRedGeneral || !existsBlackGeneral) {
                        if (EPiece.GENERAL.name().equals(pieceDTO.getName())) {
                            if (pieceDTO.isRed()) {
                                existsRedGeneral = true;
                            } else {
                                existsBlackGeneral = true;
                            }
                        }
                    }

                    if (col != pieceDTO.getCurrentCol() || row != pieceDTO.getCurrentRow()) {
                        Map<String, Object> errors = new HashMap<>();
                        errors.put("pieceDTO", pieceDTO);
                        errors.put("col", col);
                        errors.put("row", row);

                        throw new InvalidException(errors);
                    }
                }
            }
        }

        if (existsRedGeneral) {
            throw new InvalidException(Collections.singletonMap("message", "Red general is not found in board"));
        }
        
        if (existsBlackGeneral) {
            throw new InvalidException(Collections.singletonMap("message", "Black general is not found in board"));
        }
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

}
