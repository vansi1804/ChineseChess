package com.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.Default;
import com.common.enumeration.EPiece;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.data.entity.MoveHistory;
import com.config.exception.ResourceNotFoundException;
import com.data.mapper.PieceMapper;
import com.data.repository.MoveHistoryRepository;
import com.data.repository.PieceRepository;
import com.service.PieceService;

@Service
public class PieceServiceImpl implements PieceService {

    private final PieceRepository pieceRepository;
    private final PieceMapper pieceMapper;
    private final MoveHistoryRepository moveHistoryRepository;

    @Autowired
    public PieceServiceImpl(
            PieceRepository pieceRepository,
            PieceMapper pieceMapper,
            MoveHistoryRepository moveHistoryRepository) {

        this.pieceRepository = pieceRepository;
        this.pieceMapper = pieceMapper;
        this.moveHistoryRepository = moveHistoryRepository;
    }

    @Override
    public List<PieceDTO> findAll() {
        return pieceRepository.findAll().stream()
                .map(p -> pieceMapper.toDTO(p))
                .collect(Collectors.toList());
    }

    @Override
    public PieceDTO findById(int id) {
        return pieceRepository.findById(id)
                .map(p -> pieceMapper.toDTO(p))
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                Collections.singletonMap("id", id)));
    }

    @Override
    public EPiece convertByName(String name) {
        return Arrays.stream(EPiece.values())
                .filter(ePiece -> ePiece.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                Collections.singletonMap("piece.name", name)));
    }

    @Override
    public List<PieceDTO> findAllInBoard(
            PlayBoardDTO playBoardDTO, String name, Boolean isRed, int fromCol, int fromRow, int toCol, int toRow) {

        return IntStream.rangeClosed(fromCol, toCol)
                .boxed()
                .flatMap(col -> IntStream.rangeClosed(fromRow, toRow)
                        .filter(row -> {
                            PieceDTO pieceDTO = playBoardDTO.getState()[col][row];
                            return pieceDTO != null
                                    && (StringUtils.isBlank(name) || pieceDTO.getName().equals(name))
                                    && (isRed == null || pieceDTO.isRed() == isRed);
                        })
                        .mapToObj(row -> playBoardDTO.getState()[col][row]))
                .toList();
    }

    @Override
    public List<PieceDTO> findAllInBoard(PlayBoardDTO playBoardDTO, String name, Boolean isRed) {
        int fromCol = 0;
        int fromRow = 0;
        int toCol = playBoardDTO.getState().length - 1;
        int toRow = playBoardDTO.getState()[0].length - 1;

        return findAllInBoard(playBoardDTO, name, isRed, fromCol, fromRow, toCol, toRow);
    }

    @Override
    public List<PieceDTO> findAllDeadInPlayBoard(PlayBoardDTO playBoardDTO) {
        List<PieceDTO> piecesInBoard = findAllInBoard(playBoardDTO, null, null);
        List<PieceDTO> deadPieces = findAll();
        // remove all alive pieces in board
        deadPieces.removeIf(
                deadPiece -> piecesInBoard.stream().map(alivePiece -> alivePiece.getId()).toList()
                        .contains(deadPiece.getId()));

        return deadPieces;
    }

    @Override
    public PieceDTO findOneInBoard(PlayBoardDTO playBoardDTO, int id) {
        int fromCol = 0;
        int fromRow = 0;
        int toCol = playBoardDTO.getState().length - 1;
        int toRow = playBoardDTO.getState()[0].length - 1;

        return IntStream.rangeClosed(fromCol, toCol)
                .boxed()
                .flatMap(col -> IntStream.rangeClosed(fromRow, toRow)
                        .filter(row -> playBoardDTO.getState()[col][row] != null
                                && playBoardDTO.getState()[col][row].getId() == id)
                        .mapToObj(row -> playBoardDTO.getState()[col][row]))
                .findFirst()
                .orElse(null);
    }

    @Override
    public PieceDTO findLastAtPosition(long matchId, long turn, int toCol, int toRow) {
        Optional<MoveHistory> lastMoveToPosition = moveHistoryRepository
                .findFirstByMatch_IdAndToColAndToRowAndTurnLessThanEqualOrderByTurnDesc(matchId, toCol, toRow, turn);

        return lastMoveToPosition.isPresent()
                ? pieceMapper.toDTO(lastMoveToPosition.get().getPiece())
                : pieceRepository.findByCurrentColAndCurrentRow(toCol, toRow)
                        .map(p -> pieceMapper.toDTO(p))
                        .orElse(null);
    }

    @Override
    public PieceDTO findExistingTheSameInColPath(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO) {
        int col = pieceDTO.getCurrentCol();
        int fromRow = 0;
        int toRow = playBoardDTO.getState()[0].length - 1;

        return IntStream.rangeClosed(fromRow, toRow)
                .filter(row -> {
                    PieceDTO currentPiece = playBoardDTO.getState()[col][row];
                    return currentPiece != null
                            && currentPiece.getId() != pieceDTO.getId()
                            && currentPiece.isRed() == pieceDTO.isRed()
                            && currentPiece.getName().equals(pieceDTO.getName());
                })
                .mapToObj(row -> playBoardDTO.getState()[col][row])
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean existsBetweenInRowPath(PlayBoardDTO playBoardDTO, int currentRow, int fromCol, int toCol) {
        int startCol = Math.min(fromCol, toCol) + 1;
        int endCol = Math.max(fromCol, toCol) - 1;

        return IntStream.rangeClosed(startCol, endCol)
                .anyMatch(col -> playBoardDTO.getState()[col][currentRow] != null);
    }

    @Override
    public boolean existsBetweenInColPath(PlayBoardDTO playBoardDTO, int currentCol, int fromRow, int toRow) {
        int startRow = Math.min(fromRow, toRow) + 1;
        int endRow = Math.max(fromRow, toRow) - 1;

        return IntStream.rangeClosed(startRow, endRow)
                .anyMatch(row -> playBoardDTO.getState()[currentCol][row] != null);
    }

    @Override
    public int countBetweenInRowPath(PlayBoardDTO playBoardDTO, int currentRow, int fromCol, int toCol) {
        int startCol = Math.min(fromCol, toCol) + 1;
        int endCol = Math.max(fromCol, toCol) - 1;

        return (int) IntStream.rangeClosed(startCol, endCol)
                .filter(col -> playBoardDTO.getState()[col][currentRow] != null)
                .count();
    }

    @Override
    public int countBetweenInColPath(PlayBoardDTO playBoardDTO, int currentCol, int fromRow, int toRow) {
        int startRow = Math.min(fromRow, toRow) + 1;
        int endRow = Math.max(fromRow, toRow) - 1;

        return (int) IntStream.rangeClosed(startRow, endRow)
                .filter(row -> playBoardDTO.getState()[currentCol][row] != null)
                .count();
    }

    @Override
    public PieceDTO findGeneralInBoard(PlayBoardDTO playBoardDTO, boolean isRed) {
        int fromCol = Default.Game.PlayBoardSize.CENTER_COL_MIN - 1;
        int toCol = Default.Game.PlayBoardSize.CENTER_COL_MAX - 1;
        int fromRow;
        int toRow;

        if (isRed) {
            fromRow = Default.Game.PlayBoardSize.RedArea.CENTER_ROW_MIN - 1;
            toRow = Default.Game.PlayBoardSize.RedArea.CENTER_ROW_MAX - 1;
        } else {
            fromRow = Default.Game.PlayBoardSize.BlackArea.CENTER_ROW_MIN - 1;
            toRow = Default.Game.PlayBoardSize.BlackArea.CENTER_ROW_MAX - 1;
        }

        return findAllInBoard(playBoardDTO, EPiece.GENERAL.name(), null, fromCol, fromRow, toCol, toRow).stream()
                .findFirst()
                .orElse(null);
    }

}
