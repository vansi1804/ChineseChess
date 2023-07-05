package com.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public PieceServiceImpl(PieceRepository pieceRepository,
            PieceMapper pieceMapper,
            MoveHistoryRepository moveHistoryRepository) {
        this.pieceRepository = pieceRepository;
        this.pieceMapper = pieceMapper;
        this.moveHistoryRepository = moveHistoryRepository;
    }

    @Override
    public List<PieceDTO> findAll() {
        return pieceRepository.findAll().stream().map(p -> pieceMapper.toDTO(p)).collect(Collectors.toList());
    }

    @Override
    public PieceDTO findById(int id) {
        return pieceRepository.findById(id)
                .map(p -> pieceMapper.toDTO(p))
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id)));
    }

    @Override
    public EPiece convertByName(String name) {
        for (EPiece piece : EPiece.values()) {
            if (piece.getFullNameValue().equalsIgnoreCase(name)) {
                return piece;
            }
        }
        throw new ResourceNotFoundException(Collections.singletonMap("name", name));
    }

    @Override
    public List<PieceDTO> findAllInBoard(PlayBoardDTO playBoardDTO, String name, Boolean isRed) {
        return IntStream.range(0, playBoardDTO.getState().length)
                .boxed()
                .flatMap(col -> IntStream.range(0, playBoardDTO.getState()[col].length)
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
    public List<PieceDTO> findAllDeadInPlayBoard(PlayBoardDTO playBoardDTO) {
        List<PieceDTO> piecesInBoard = findAllInBoard(playBoardDTO, null, null);
        List<PieceDTO> deadPieces = findAll();
        // remove all alive pieces in board
        deadPieces.removeIf(p1 -> piecesInBoard.stream().map(p2 -> p2.getId()).toList().contains(p1.getId()));

        return deadPieces;
    }

    @Override
    public PieceDTO findOneInBoard(PlayBoardDTO playBoardDTO, int id) {
        return IntStream.range(0, playBoardDTO.getState().length)
                .boxed()
                .flatMap(col -> IntStream.range(0, playBoardDTO.getState()[col].length)
                        .filter(row -> playBoardDTO.getState()[col][row] != null
                                && playBoardDTO.getState()[col][row].getId() == id)
                        .mapToObj(row -> playBoardDTO.getState()[col][row]))
                .findFirst()
                .orElse(null);
    }

    @Override
    public PieceDTO findLastAtPosition(long matchId, long turn, int toCol, int toRow) {
        Optional<MoveHistory> lastMoveToPosition = moveHistoryRepository
                .findLastByMatchIdAndPositionUntilTurn(matchId, turn, toCol, toRow);

        return lastMoveToPosition.isPresent()
                ? pieceMapper.toDTO(lastMoveToPosition.get().getPiece())
                : pieceRepository.findByCurrentColAndCurrentRow(toCol, toRow)
                        .map(p -> pieceMapper.toDTO(p))
                        .orElse(null);
    }

    @Override
    public PieceDTO findExistingTheSameInColPath(PlayBoardDTO playBoard, PieceDTO pieceDTO) {
        return IntStream.rangeClosed(1, playBoard.getState()[0].length)
                .filter(row -> {
                    PieceDTO currentPiece = playBoard.getState()[pieceDTO.getCurrentCol() - 1][row - 1];
                    return currentPiece != null
                            && currentPiece.getId() != pieceDTO.getId()
                            && currentPiece.isRed() == pieceDTO.isRed()
                            && currentPiece.getName().equals(pieceDTO.getName());
                })
                .mapToObj(row -> playBoard.getState()[pieceDTO.getCurrentCol() - 1][row - 1])
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean existsBetweenInRowPath(PlayBoardDTO playBoard, int currentRow, int fromCol, int toCol) {
        int startCol = Math.min(fromCol, toCol) + 1;
        int endCol = Math.max(fromCol, toCol) - 1;

        return IntStream.rangeClosed(startCol, endCol)
                .anyMatch(col -> playBoard.getState()[col - 1][currentRow - 1] != null);
    }

    @Override
    public boolean existsBetweenInColPath(PlayBoardDTO playBoard, int currentCol, int fromRow, int toRow) {
        int startRow = Math.min(fromRow, toRow) + 1;
        int endRow = Math.max(fromRow, toRow) - 1;

        return IntStream.rangeClosed(startRow, endRow)
                .anyMatch(row -> playBoard.getState()[currentCol - 1][row - 1] != null);
    }

    @Override
    public int countBetweenInRowPath(PlayBoardDTO playBoard, int currentRow, int fromCol, int toCol) {
        int startCol = Math.min(fromCol, toCol) + 1;
        int endCol = Math.max(fromCol, toCol) - 1;

        return (int) IntStream.rangeClosed(startCol, endCol)
                .filter(col -> playBoard.getState()[col - 1][currentRow - 1] != null)
                .count();
    }

    @Override
    public int countBetweenInColPath(PlayBoardDTO playBoard, int currentCol, int fromRow, int toRow) {
        int startRow = Math.min(fromRow, toRow) + 1;
        int endRow = Math.max(fromRow, toRow) - 1;

        return (int) IntStream.rangeClosed(startRow, endRow)
                .filter(row -> playBoard.getState()[currentCol - 1][row - 1] != null)
                .count();
    }

}
