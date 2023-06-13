package com.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.Default;
import com.common.enumeration.EPiece;
import com.data.dto.MatchCreationResponseDTO;
import com.data.dto.MoveHistoryCreationDTO;
import com.data.dto.MoveHistoryDTO;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.data.entity.Match;
import com.data.entity.MoveHistory;
import com.data.mapper.MoveHistoryMapper;
import com.data.repository.MatchRepository;
import com.data.repository.MoveHistoryRepository;
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
    private PieceService pieceService;
    @Autowired
    private MovingRuleService movingRuleService;

    @Override
    public List<MoveHistoryDTO> findAllByMatchId(long matchId) {
        AtomicReference<PlayBoardDTO> currentBoard = new AtomicReference<>(playBoardService.create());
        List<MoveHistory> moveHistories = moveHistoryRepository.findAllByMatch_Id(matchId);

        return moveHistories.stream()
                .map(mh -> {
                    PieceDTO movingPieceDTO = pieceService.findOneInBoard(currentBoard.get(), mh.getPiece().getId());

                    MoveHistoryDTO moveHistoryDTO = new MoveHistoryDTO();

                    moveHistoryDTO.setTurn(mh.getTurn());
                    moveHistoryDTO.setDescription(moveDescriptionService.buildDescription(
                            currentBoard.get(), movingPieceDTO, mh.getToCol(), mh.getToRow()));

                    currentBoard.set(
                            playBoardService.update(currentBoard.get(), movingPieceDTO, mh.getToCol(), mh.getToRow()));

                    moveHistoryDTO.setCurrentBoard(currentBoard.get());
                    moveHistoryDTO.setCurrentDeadPieceDTOs(
                            pieceService.findAllDeadInPlayBoard(currentBoard.get()));
                    moveHistoryDTO.setGeneralBeingChecked(
                            findGeneralBeingChecked(currentBoard.get(), !movingPieceDTO.isRed()));

                    return moveHistoryDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean[][] findMoveValid(long matchId, int pieceId) {
        if (!matchRepository.existsById(matchId)) {
            new ResourceNotFoundException(Collections.singletonMap("matchId", matchId));
        }

        PieceDTO movingPieceDTO = pieceService.findById(pieceId);

        long lastTurn = moveHistoryRepository.countTurnByMatch_Id(matchId);
        long newTurn = lastTurn + 1;

        PlayBoardDTO currentBoard = buildPlayBoardByMatchId(matchId);

        PieceDTO foundPieceInBoard = pieceService.findOneInBoard(currentBoard, movingPieceDTO.getId());
        if (foundPieceInBoard == null) {
            int[] lastPosition = moveHistoryRepository.findLastPositionByMatchIdAndPieceId(
                    matchId, movingPieceDTO.getId());

            if (lastPosition != null) {
                int lastCol = lastPosition[0];
                int lastRow = lastPosition[1];
                movingPieceDTO.setCurrentCol(lastCol);
                movingPieceDTO.setCurrentRow(lastRow);
            }

            Map<String, Object> errors = new HashMap<>();
            errors.put("matchId", matchId);
            errors.put("turn", newTurn);
            errors.put("pieceDTO", movingPieceDTO);
            throw new DeadPieceException(errors);

        } else {
            int currentCol = foundPieceInBoard.getCurrentCol();
            int currentRow = foundPieceInBoard.getCurrentRow();
            movingPieceDTO.setCurrentCol(currentCol);
            movingPieceDTO.setCurrentRow(currentRow);
        }

        boolean[][] validMoves = new boolean[MAX_COL][MAX_ROW];
        for (int toCol = 1; toCol <= MAX_COL; toCol++) {
            for (int toRow = 1; toRow <= MAX_ROW; toRow++) {
                validMoves[toCol - 1][toRow - 1] = movingRuleService.isMoveValid(
                        currentBoard, movingPieceDTO, toCol, toRow);
            }
        }

        return validMoves;
    }

    @Override
    public MatchCreationResponseDTO create(MoveHistoryCreationDTO moveHistoryCreationDTO) {
        Match match = matchRepository.findById(moveHistoryCreationDTO.getMatchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Collections.singletonMap("matchId", moveHistoryCreationDTO.getMatchId())));

        if ((match.getPlayer1().getId() != moveHistoryCreationDTO.getPlayerId())
                && (match.getPlayer2().getId() != moveHistoryCreationDTO.getPlayerId())) {
            throw new InvalidMovingPlayerException(
                    Collections.singletonMap("playerId", moveHistoryCreationDTO.getPlayerId()));
        }

        long lastTurn = moveHistoryRepository.countTurnByMatch_Id(match.getId());
        long newTurn = lastTurn + 1;

        PieceDTO movingPieceDTO = pieceService.findById(moveHistoryCreationDTO.getPieceId());

        PlayBoardDTO currentBoard = this.buildPlayBoardByMatchId(match.getId());

        // check piece input existing in currentBoard
        PieceDTO foundPieceInBoard = pieceService.findOneInBoard(currentBoard, movingPieceDTO.getId());
        if (foundPieceInBoard == null) {
            int[] lastPosition = moveHistoryRepository.findLastPositionByMatchIdAndPieceId(match.getId(),
                    movingPieceDTO.getId());
            if (lastPosition != null) {
                movingPieceDTO.setCurrentCol(lastPosition[0]);
                movingPieceDTO.setCurrentCol(lastPosition[1]);
            }

            Map<String, Object> errors = new HashMap<>();
            errors.put("matchId", match.getId());
            errors.put("turn", newTurn);
            errors.put("pieceDTO", movingPieceDTO);
            throw new DeadPieceException(errors);

        } else {
            movingPieceDTO.setCurrentCol(foundPieceInBoard.getCurrentCol());
            movingPieceDTO.setCurrentRow(foundPieceInBoard.getCurrentRow());
        }

        if (((newTurn % 2 != 0) && (match.getPlayer1().getId() != moveHistoryCreationDTO.getPlayerId()))
                || ((newTurn % 2 == 0) && (match.getPlayer2().getId() != moveHistoryCreationDTO.getPlayerId()))) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("matchId", match.getId());
            errors.put("turn", newTurn);
            errors.put("pieceDTO", movingPieceDTO);
            throw new OpponentTurnException(errors);
        }

        if (movingPieceDTO.isRed() && (match.getPlayer1().getId() != moveHistoryCreationDTO.getPlayerId())) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("matchId", match.getId());
            errors.put("playerId", moveHistoryCreationDTO.getPieceId());
            errors.put("turn", newTurn);
            errors.put("pieceDTO", movingPieceDTO);
            throw new InvalidPlayerMovePieceException(errors);
        }

        boolean isValidMove = movingRuleService.isMoveValid(
                currentBoard, movingPieceDTO, moveHistoryCreationDTO.getToCol(), moveHistoryCreationDTO.getToRow());
        if (isValidMove) {
            MoveHistory moveHistory = moveHistoryMapper.toEntity(moveHistoryCreationDTO);
            moveHistory.setTurn(newTurn);
            moveHistory.setFromCol(movingPieceDTO.getCurrentCol());
            moveHistory.setFromRow(movingPieceDTO.getCurrentRow());
            moveHistoryRepository.save(moveHistory);

            currentBoard = playBoardService.update(
                    currentBoard, movingPieceDTO, moveHistoryCreationDTO.getToCol(), moveHistoryCreationDTO.getToRow());

            MatchCreationResponseDTO matchCreationResponseDTO = new MatchCreationResponseDTO();

            matchCreationResponseDTO.setGeneralBeingChecked(
                    findGeneralBeingChecked(currentBoard, !movingPieceDTO.isRed()));
            matchCreationResponseDTO.setDeadPiecesDTO(pieceService.findAllDeadInPlayBoard(currentBoard));

            return matchCreationResponseDTO;
        }

        Map<String, Object> errors = new HashMap<>();
        errors.put("matchId", match.getId());
        errors.put("turn", newTurn);
        errors.put("pieceDTO", movingPieceDTO);
        errors.put("toCol", moveHistoryCreationDTO.getToCol());
        errors.put("toRow", moveHistoryCreationDTO.getToRow());
        throw new InvalidMoveException(errors);
    }

    private PlayBoardDTO buildPlayBoardByMatchId(long matchId) {
        return moveHistoryRepository.findAllByMatch_Id(matchId).stream()
                .reduce(playBoardService.create(), (board, mh) -> playBoardService.update(
                        board, pieceService.findOneInBoard(board, mh.getPiece().getId()), mh.getToCol(), mh.getToRow()),
                        (board1, board2) -> board2);
    }

    private PieceDTO findGeneralBeingChecked(PlayBoardDTO playBoard, boolean isRed) {
        // check opponent general is being check after moving
        List<PieceDTO> generals = pieceService.findAllInBoard(playBoard, EPiece.General.getFullNameValue(), isRed);
        PieceDTO general = generals.isEmpty() ? null : generals.get(0);
        return movingRuleService.isGeneralBeingChecked(playBoard, general) ? general : null;
    }

}
