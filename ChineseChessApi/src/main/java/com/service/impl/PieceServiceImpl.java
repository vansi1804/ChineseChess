package com.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.enumeration.EPiece;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.data.entity.MoveHistory;
import com.data.entity.Piece;
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
        return Arrays.stream(EPiece.values())
                .filter(ePiece -> ePiece.getFullName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("piece.name", name)));
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
                                    && (isRed == null || pieceDTO.getColor() == isRed);
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
                            && currentPiece.getColor() == pieceDTO.getColor()
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

    @PostConstruct
    public void init() {
        List<Piece> defaultPieces = new ArrayList<>();
        // Red pieces
        defaultPieces.add(
                new Piece(1, EPiece.Soldier.getFullName(), Boolean.TRUE, "red_soldier.png", 1, 7));
        defaultPieces.add(
                new Piece(2, EPiece.Soldier.getFullName(), Boolean.TRUE, "red_soldier.png", 3, 7));
        defaultPieces.add(
                new Piece(3, EPiece.Soldier.getFullName(), Boolean.TRUE, "red_soldier.png", 5, 7));
        defaultPieces.add(
                new Piece(4, EPiece.Soldier.getFullName(), Boolean.TRUE, "red_soldier.png", 7, 7));
        defaultPieces.add(
                new Piece(5, EPiece.Soldier.getFullName(), Boolean.TRUE, "red_soldier.png", 9, 7));
        defaultPieces.add(
                new Piece(6, EPiece.Cannon.getFullName(), Boolean.TRUE, "red_cannon.png", 2, 8));
        defaultPieces.add(
                new Piece(7, EPiece.Cannon.getFullName(), Boolean.TRUE, "red_cannon.png", 8, 8));
        defaultPieces.add(
                new Piece(8, EPiece.Chariot.getFullName(), Boolean.TRUE, "red_chariot.png", 1, 10));
        defaultPieces.add(
                new Piece(9, EPiece.Chariot.getFullName(), Boolean.TRUE, "red_chariot.png", 9, 10));
        defaultPieces.add(
                new Piece(10, EPiece.Horse.getFullName(), Boolean.TRUE, "red_horse.png", 2, 10));
        defaultPieces.add(
                new Piece(11, EPiece.Horse.getFullName(), Boolean.TRUE, "red_horse.png", 8, 10));
        defaultPieces.add(
                new Piece(12, EPiece.Elephant.getFullName(), Boolean.TRUE, "red_elephant.png", 3, 10));
        defaultPieces.add(
                new Piece(13, EPiece.Elephant.getFullName(), Boolean.TRUE, "red_elephant.png", 7, 10));
        defaultPieces.add(
                new Piece(14, EPiece.Guard.getFullName(), Boolean.TRUE, "red_guard.png", 4, 10));
        defaultPieces.add(
                new Piece(15, EPiece.Guard.getFullName(), Boolean.TRUE, "red_guard.png", 6, 10));
        defaultPieces.add(
                new Piece(16, EPiece.General.getFullName(), Boolean.TRUE, "red_general.png", 5, 10));

        // Black pieces
        defaultPieces.add(
                new Piece(17, EPiece.Soldier.getFullName(), Boolean.FALSE, "black_soldier.png", 1, 4));
        defaultPieces.add(
                new Piece(18, EPiece.Soldier.getFullName(), Boolean.FALSE, "black_soldier.png", 3, 4));
        defaultPieces.add(
                new Piece(19, EPiece.Soldier.getFullName(), Boolean.FALSE, "black_soldier.png", 5, 4));
        defaultPieces.add(
                new Piece(20, EPiece.Soldier.getFullName(), Boolean.FALSE, "black_soldier.png", 7, 4));
        defaultPieces.add(
                new Piece(21, EPiece.Soldier.getFullName(), Boolean.FALSE, "black_soldier.png", 9, 4));
        defaultPieces.add(
                new Piece(22, EPiece.Cannon.getFullName(), Boolean.FALSE, "black_cannon.png", 2, 3));
        defaultPieces.add(
                new Piece(23, EPiece.Cannon.getFullName(), Boolean.FALSE, "black_cannon.png", 8, 3));
        defaultPieces.add(
                new Piece(24, EPiece.Chariot.getFullName(), Boolean.FALSE, "black_chariot.png", 1, 1));
        defaultPieces.add(
                new Piece(25, EPiece.Chariot.getFullName(), Boolean.FALSE, "black_chariot.png", 9, 1));
        defaultPieces.add(
                new Piece(26, EPiece.Horse.getFullName(), Boolean.FALSE, "black_horse.png", 2, 1));
        defaultPieces.add(
                new Piece(27, EPiece.Horse.getFullName(), Boolean.FALSE, "black_horse.png", 8, 1));
        defaultPieces.add(
                new Piece(28, EPiece.Elephant.getFullName(), Boolean.FALSE, "black_elephant.png", 3, 1));
        defaultPieces.add(
                new Piece(29, EPiece.Elephant.getFullName(), Boolean.FALSE, "black_elephant.png", 7, 1));
        defaultPieces.add(
                new Piece(30, EPiece.Guard.getFullName(), Boolean.FALSE, "black_guard.png", 4, 1));
        defaultPieces.add(
                new Piece(31, EPiece.Guard.getFullName(), Boolean.FALSE, "black_guard.png", 6, 1));
        defaultPieces.add(
                new Piece(32, EPiece.General.getFullName(), Boolean.FALSE, "black_general.png", 5, 1));

        pieceRepository.saveAll(defaultPieces);

    }

}
