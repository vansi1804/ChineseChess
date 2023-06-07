package com.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.Default;
import com.data.dto.MoveHistoryCreationDTO;
import com.data.dto.MoveHistoryDTO;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.data.entity.Match;
import com.data.entity.MoveHistory;
import com.data.mapper.MoveHistoryMapper;
import com.data.mapper.PieceMapper;
import com.data.repository.MatchRepository;
import com.data.repository.MoveHistoryRepository;
import com.data.repository.PieceRepository;
import com.exception.DeadPieceException;
import com.exception.InvalidMoveException;
import com.exception.InvalidMovingPlayerException;
import com.exception.InvalidPlayerMovePieceException;
import com.exception.OpponentTurnException;
import com.exception.ResourceNotFoundException;
import com.service.MoveHistoryService;
import com.service.MovingRuleService;
import com.service.PieceService;
import com.service.MoveDescriptionService;
import com.service.PlayBoardService;

@Service
public class MoveHistoryServiceImpl implements MoveHistoryService {
    private final int MAX_COL = Default.Game.PlayBoardSize.COL;
    private final int MAX_ROW = Default.Game.PlayBoardSize.ROW;

    @Autowired
    private MoveHistoryRepository moveHistoryRepository;
    @Autowired
    private MoveHistoryMapper moveHistoryMapper;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private PlayBoardService playBoardService;
    @Autowired
    private MoveDescriptionService moveDescriptionService;
    @Autowired
    private PieceRepository pieceRepository;
    @Autowired
    private PieceMapper pieceMapper;
    @Autowired
    private PieceService pieceService;
    @Autowired
    private MovingRuleService movingRuleService;

    @Override
    public List<MoveHistoryDTO> findAllByMatchId(long matchId) {
        PlayBoardDTO currentBoard = playBoardService.create();
        List<PieceDTO> currentDeadPieces = new ArrayList<>();

        return moveHistoryRepository.findAllByMatch_Id(matchId)
                .stream()
                .map(mh -> {
                    MoveHistoryDTO moveHistoryDTO = new MoveHistoryDTO();
                    moveHistoryDTO.setTurn(mh.getTurn());

                    PieceDTO deadPiece = findLastTargetPiece(mh.getMatch().getId(), mh.getTurn() - 1,
                            mh.getToCol(), mh.getToRow());
                    if (deadPiece != null) {
                        currentDeadPieces.add(deadPiece);
                    }

                    moveHistoryDTO.setCurrentDeadPieceDTOs(new ArrayList<>(currentDeadPieces));

                    PieceDTO movingPiece = pieceMapper.toDTO(mh.getPiece());
                    // check piece input existing in currentBoard
                    int[] pieceFoundAtPosition = findPositionOfPieceAtCurrentPlayBoard(
                            currentBoard, mh.getPiece().getId());

                    if (pieceFoundAtPosition == null) {
                        int[] lastPosition = moveHistoryRepository.findLastPositionByMatchIdAndPieceId(matchId,
                                mh.getPiece().getId());
                        if (lastPosition != null) {
                            movingPiece.setCurrentCol(lastPosition[0]);
                            movingPiece.setCurrentCol(lastPosition[1]);
                        }
                    } else {
                        movingPiece.setCurrentCol(pieceFoundAtPosition[0]);
                        movingPiece.setCurrentRow(pieceFoundAtPosition[1]);
                    }

                    moveHistoryDTO.setDescription(moveDescriptionService.buildDescription(
                            currentBoard, movingPiece, mh.getToCol(), mh.getToRow()));
                    moveHistoryDTO.setCurrentBoard(playBoardService.update(currentBoard, mh));
                    return moveHistoryDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean[][] findMoveValid(long matchId, int pieceId) {
        if (!matchRepository.existsById(matchId)) {
            new ResourceNotFoundException(
                    Collections.singletonMap("matchId", matchId));
        }
        PieceDTO pieceDTO = pieceService.findById(pieceId);

        long lastTurn = moveHistoryRepository.countTurnByMatch_Id(matchId);
        long newTurn = lastTurn + 1;

        PlayBoardDTO currentBoard = buildCurrentBoardByMatchIdAndTurn(matchId, lastTurn);

        // check piece input existing in currentBoard
        int[] pieceFoundAtPosition = findPositionOfPieceAtCurrentPlayBoard(currentBoard, pieceDTO.getId());

        if (pieceFoundAtPosition != null) {
            pieceDTO.setCurrentCol(pieceFoundAtPosition[0]);
            pieceDTO.setCurrentRow(pieceFoundAtPosition[1]);
        } else {
            int[] lastPosition = moveHistoryRepository.findLastPositionByMatchIdAndPieceId(matchId, pieceDTO.getId());

            // check piece has moved
            if (lastPosition.length > 0) {
                pieceDTO.setCurrentCol(lastPosition[0]);
                pieceDTO.setCurrentRow(lastPosition[1]);
            }

            Map<String, Object> errors = new HashMap<>();
            errors.put("matchId", matchId);
            errors.put("turn", newTurn);
            errors.put("pieceDTO", pieceDTO);
            throw new DeadPieceException(errors);
        }

        boolean[][] validMove = new boolean[MAX_COL][MAX_ROW];
        for (int toCol = 1; toCol <= MAX_COL; toCol++) {
            for (int toRow = 1; toRow <= MAX_ROW; toRow++) {
                validMove[toCol - 1][toRow - 1] = movingRuleService.isMoveValid(
                        currentBoard, pieceDTO, toCol, toRow);
            }
        }

        return validMove;
    }

    @Override
    public List<PieceDTO> create(MoveHistoryCreationDTO moveHistoryCreationDTO) {
        Match match = matchRepository.findById(moveHistoryCreationDTO.getMatchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Collections.singletonMap("matchId", moveHistoryCreationDTO.getMatchId())));

        if ((match.getPlayer1().getId() != moveHistoryCreationDTO.getPlayerId())
                && (match.getPlayer2().getId() != moveHistoryCreationDTO.getPlayerId())) {
            throw new InvalidMovingPlayerException(
                    Collections.singletonMap("playerId", moveHistoryCreationDTO.getPlayerId()));
        }

        PieceDTO pieceDTO = pieceService.findById(moveHistoryCreationDTO.getPieceId());

        long lastTurn = moveHistoryRepository.countTurnByMatch_Id(match.getId());
        long newTurn = lastTurn + 1;

        if (((newTurn % 2 != 0) && (match.getPlayer1().getId() != moveHistoryCreationDTO.getPlayerId()))
                || ((newTurn % 2 == 0) && (match.getPlayer2().getId() != moveHistoryCreationDTO.getPlayerId()))) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("matchId", match.getId());
            errors.put("turn", newTurn);
            throw new OpponentTurnException(errors);
        }

        PlayBoardDTO currentBoard = buildCurrentBoardByMatchIdAndTurn(match.getId(), lastTurn);
        int[] pieceFoundAtPosition = findPositionOfPieceAtCurrentPlayBoard(currentBoard,
                moveHistoryCreationDTO.getPieceId());

        // check piece input existing in currentBoard
        if (pieceFoundAtPosition != null) {
            pieceDTO.setCurrentCol(pieceFoundAtPosition[0]);
            pieceDTO.setCurrentRow(pieceFoundAtPosition[1]);
        } else {

            int[] lastPosition = moveHistoryRepository.findLastPositionByMatchIdAndPieceId(
                    moveHistoryCreationDTO.getMatchId(), pieceDTO.getId());
            if (lastPosition != null) {
                pieceDTO.setCurrentCol(lastPosition[0]);
                pieceDTO.setCurrentCol(lastPosition[1]);
            }

            Map<String, Object> errors = new HashMap<>();
            errors.put("matchId", moveHistoryCreationDTO.getMatchId());
            errors.put("turn", newTurn);
            errors.put("pieceDTO", pieceDTO);
            throw new DeadPieceException(errors);
        }

        if (pieceDTO.isRed() && (match.getPlayer1().getId() != moveHistoryCreationDTO.getPlayerId())) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("matchId", match.getId());
            errors.put("playerId", moveHistoryCreationDTO.getPieceId());
            errors.put("turn", newTurn);
            errors.put("pieceDTO", pieceDTO);
            throw new InvalidPlayerMovePieceException(errors);
        }

        if (movingRuleService.isMoveValid(currentBoard, pieceDTO,
                moveHistoryCreationDTO.getToCol(), moveHistoryCreationDTO.getToRow())) {
            MoveHistory moveHistory = moveHistoryMapper.toEntity(moveHistoryCreationDTO);
            moveHistory.setTurn(newTurn);
            moveHistory.setFromCol(pieceDTO.getCurrentCol());
            moveHistory.setFromRow(pieceDTO.getCurrentRow());
            moveHistoryRepository.save(moveHistory);

            return findCurrentDeadPiecesByMatchId(match.getId());
        } else {
            Map<String, Object> errors = new HashMap<>();
            errors.put("matchId", match.getId());
            errors.put("turn", newTurn);
            errors.put("pieceDTO", pieceDTO);
            errors.put("toCol", moveHistoryCreationDTO.getToCol());
            errors.put("toRow", moveHistoryCreationDTO.getToRow());
            throw new InvalidMoveException(errors);
        }
    }

    private PieceDTO findLastTargetPiece(long matchId, long turn, int col, int row) {
        Optional<MoveHistory> lastMoveToPosition = moveHistoryRepository
                .findLastByMatchIdAndPositionUntilTurn(matchId, turn, col, row);

        return lastMoveToPosition.isPresent()
                ? pieceMapper.toDTO(lastMoveToPosition.get().getPiece())
                : pieceRepository.findByCurrentColAndCurrentRow(col, row)
                        .map(p -> {
                            PieceDTO startPiece = pieceMapper.toDTO(p);
                            startPiece.setCurrentCol(col);
                            startPiece.setCurrentRow(row);
                            return startPiece;
                        }).orElse(null);

    }

    private List<PieceDTO> findCurrentDeadPiecesByMatchId(long matchId) {
        List<PieceDTO> currentDeadPieces = new ArrayList<>();
        for (MoveHistory mh : moveHistoryRepository.findAllByMatch_Id(matchId)) {
            PieceDTO deadPiece = findLastTargetPiece(mh.getMatch().getId(), mh.getTurn() - 1,
                    mh.getToCol(), mh.getToRow());
            if (deadPiece != null) {
                currentDeadPieces.add(deadPiece);
            }
        }
        return currentDeadPieces;
    }

    PlayBoardDTO buildCurrentBoardByMatchIdAndTurn(long matchId, long turn) {
        if (turn > 0) {
            int index = (int) (turn - 1);
            MoveHistoryDTO lastedMoveHistoryDTO = findAllByMatchId(matchId).get(index);
            return lastedMoveHistoryDTO.getCurrentBoard();
        }
        return playBoardService.create();
    }

    int[] findPositionOfPieceAtCurrentPlayBoard(PlayBoardDTO currentBoard, int pieceId) {
        return IntStream.range(0, currentBoard.getState().length)
                .boxed()
                .flatMap(col -> IntStream.range(0, currentBoard.getState()[col].length)
                        .filter(row -> currentBoard.getState()[col][row] != null
                                && currentBoard.getState()[col][row].getId() == pieceId)
                        .mapToObj(row -> new int[] { col + 1, row + 1 }))
                .findFirst()
                .orElse(null);
    }

}
