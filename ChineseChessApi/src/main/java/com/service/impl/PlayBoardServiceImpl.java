package com.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.Default;
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

    private final int MAX_COL = Default.Game.PlayBoardSize.COL;
    private final int MAX_ROW = Default.Game.PlayBoardSize.ROW;

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

        playBoardDTO.print("start: ", null);

        return playBoardDTO;
    }

    @Override
    public PlayBoardDTO buildPlayBoardByMoveHistories(List<MoveHistory> moveHistories) {
        return moveHistories.stream()
                .reduce(
                        generate(),
                        (currentBoardDTO, mh) -> update(
                                currentBoardDTO, pieceService.findOneInBoard(currentBoardDTO, mh.getPiece().getId()),
                                mh.getToCol(), mh.getToRow()),
                        (currentBoardDTO, updatedBoardDTO) -> updatedBoardDTO);
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

    private PieceDTO[][] cloneStateArray(PieceDTO[][] state) {
        return Arrays.stream(state)
                .map(PieceDTO[]::clone)
                .toArray(PieceDTO[][]::new);
    }

}
