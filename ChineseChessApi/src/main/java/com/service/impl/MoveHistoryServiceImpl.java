package com.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.Default;
import com.common.ErrorMessage;
import com.common.enumeration.EPiece;
import com.data.dto.MoveHistoryCreationResponseDTO;
import com.data.dto.MoveHistoryCreationDTO;
import com.data.dto.MoveHistoryDTO;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.data.entity.Match;
import com.data.entity.MoveHistory;
import com.data.mapper.MoveHistoryMapper;
import com.data.repository.MatchRepository;
import com.data.repository.MoveHistoryRepository;
import com.exception.InvalidException;
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

    private final MoveHistoryRepository moveHistoryRepository;
    private final MoveHistoryMapper moveHistoryMapper;
    private final MatchRepository matchRepository;
    private final PlayBoardService playBoardService;
    private final MoveDescriptionService moveDescriptionService;
    private final PieceService pieceService;
    private final MovingRuleService movingRuleService;

    @Autowired
    public MoveHistoryServiceImpl(MoveHistoryRepository moveHistoryRepository,
            MoveHistoryMapper moveHistoryMapper,
            MatchRepository matchRepository,
            PlayBoardService playBoardService,
            MoveDescriptionService moveDescriptionService,
            PieceService pieceService,
            MovingRuleService movingRuleService) {
        this.moveHistoryRepository = moveHistoryRepository;
        this.moveHistoryMapper = moveHistoryMapper;
        this.matchRepository = matchRepository;
        this.playBoardService = playBoardService;
        this.moveDescriptionService = moveDescriptionService;
        this.pieceService = pieceService;
        this.movingRuleService = movingRuleService;
    }

    @Override
    public List<MoveHistoryDTO> findAllByMatchId(long matchId) {
        AtomicReference<PlayBoardDTO> currentBoard = new AtomicReference<>(playBoardService.create());
        List<MoveHistory> moveHistories = moveHistoryRepository.findAllByMatch_Id(matchId);

        System.out.println("start:");
        currentBoard.get().print(null);

        return moveHistories.stream()
                .map(mh -> {
                    PieceDTO movingPieceDTO = pieceService.findOneInBoard(currentBoard.get(), mh.getPiece().getId());

                    MoveHistoryDTO moveHistoryDTO = new MoveHistoryDTO();

                    moveHistoryDTO.setTurn(mh.getTurn());
                    moveHistoryDTO.setMovingPieceDTO(movingPieceDTO);
                    moveHistoryDTO.setDescription(moveDescriptionService.buildDescription(
                            currentBoard.get(), movingPieceDTO, mh.getToCol(), mh.getToRow()));

                    currentBoard.set(playBoardService.update(
                            currentBoard.get(), movingPieceDTO, mh.getToCol(), mh.getToRow()));

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
            throw new InvalidException(ErrorMessage.DEAD_PIECE, errors);

        } else {
            int currentCol = foundPieceInBoard.getCurrentCol();
            int currentRow = foundPieceInBoard.getCurrentRow();
            movingPieceDTO.setCurrentCol(currentCol);
            movingPieceDTO.setCurrentRow(currentRow);
        }

        System.out.println("current");
        currentBoard.print(movingPieceDTO);

        return findValidMove(currentBoard, movingPieceDTO);
    }

    @Override
    public MoveHistoryCreationResponseDTO create(MoveHistoryCreationDTO moveHistoryCreationDTO) {
        Match match = matchRepository.findById(moveHistoryCreationDTO.getMatchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Collections.singletonMap("matchId", moveHistoryCreationDTO.getMatchId())));

        if (match.getResult() != null) {
            throw new InvalidException(ErrorMessage.END_MATCH, Collections.singletonMap("matchId", match.getId()));
        }

        if ((match.getPlayer1().getId() != moveHistoryCreationDTO.getPlayerId())
                && (match.getPlayer2().getId() != moveHistoryCreationDTO.getPlayerId())) {
            throw new InvalidException(ErrorMessage.INVALID_MOVING_PLAYER,
                    Collections.singletonMap("playerId", moveHistoryCreationDTO.getPlayerId()));
        }

        long lastTurn = moveHistoryRepository.countTurnByMatch_Id(match.getId());
        long newTurn = lastTurn + 1;

        PieceDTO movingPieceDTO = pieceService.findById(moveHistoryCreationDTO.getPieceId());

        PlayBoardDTO currentBoard = this.buildPlayBoardByMatchId(match.getId());

        // check piece input existing in currentBoard
        PieceDTO foundPieceInBoard = pieceService.findOneInBoard(currentBoard, movingPieceDTO.getId());
        if (foundPieceInBoard == null) {
            int[] lastPosition = moveHistoryRepository.findLastPositionByMatchIdAndPieceId(
                    match.getId(), movingPieceDTO.getId());
            if (lastPosition != null) {
                movingPieceDTO.setCurrentCol(lastPosition[0]);
                movingPieceDTO.setCurrentRow(lastPosition[1]);
            }

            Map<String, Object> errors = new HashMap<>();
            errors.put("matchId", match.getId());
            errors.put("turn", newTurn);
            errors.put("pieceDTO", movingPieceDTO);
            throw new InvalidException(ErrorMessage.DEAD_PIECE, errors);

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
            throw new InvalidException(ErrorMessage.OPPONENT_TURN, errors);
        }

        if (movingPieceDTO.isRed() && (match.getPlayer1().getId() != moveHistoryCreationDTO.getPlayerId())) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("matchId", match.getId());
            errors.put("playerId", moveHistoryCreationDTO.getPieceId());
            errors.put("turn", newTurn);
            errors.put("pieceDTO", movingPieceDTO);
            throw new InvalidException(ErrorMessage.INVALID_PLAYER_MOVE_PIECE, errors);
        }

        System.out.println("current");
        currentBoard.print(movingPieceDTO);

        boolean isValidMove = movingRuleService.isMoveValid(
                currentBoard, movingPieceDTO, moveHistoryCreationDTO.getToCol(), moveHistoryCreationDTO.getToRow());
        if (isValidMove) {
            MoveHistory moveHistory = moveHistoryMapper.toEntity(moveHistoryCreationDTO);
            moveHistory.setTurn(newTurn);
            moveHistoryRepository.save(moveHistory);

            MoveHistoryCreationResponseDTO moveHistoryCreationResponseDTO = new MoveHistoryCreationResponseDTO();
            moveHistoryCreationResponseDTO.setDeadPieceDTO(
                    currentBoard.getState()[moveHistory.getToCol() - 1][moveHistory.getToRow() - 1]);

            currentBoard = playBoardService.update(
                    currentBoard, movingPieceDTO, moveHistoryCreationDTO.getToCol(), moveHistoryCreationDTO.getToRow());

            moveHistoryCreationResponseDTO.setGeneralBeingChecked(
                    findGeneralBeingChecked(currentBoard, !movingPieceDTO.isRed()));
            
            boolean isCheckMate = isCheckMate(currentBoard, movingPieceDTO.isRed());
            if (isCheckMate) {
                match.setResult(moveHistoryCreationDTO.getPlayerId());
                matchRepository.save(match);
            }
            
            moveHistoryCreationResponseDTO.setCheckMate(isCheckMate);

            System.out.println("===========================================");
            System.out.println("Turn" + newTurn);
            currentBoard.print(movingPieceDTO);

            return moveHistoryCreationResponseDTO;
        }

        Map<String, Object> errors = new HashMap<>();
        errors.put("matchId", match.getId());
        errors.put("turn", newTurn);
        errors.put("pieceDTO", movingPieceDTO);
        errors.put("toCol", moveHistoryCreationDTO.getToCol());
        errors.put("toRow", moveHistoryCreationDTO.getToRow());
        throw new InvalidException(ErrorMessage.INVALID_MOVE, errors);
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

    private boolean isCheckMate(PlayBoardDTO playBoard, boolean isRed) {
        List<PieceDTO> opponentPiecesInBoard = pieceService.findAllInBoard(playBoard, null, !isRed);
        for (PieceDTO opponentPiece : opponentPiecesInBoard) {
            boolean[][] validMoves = findValidMove(playBoard, opponentPiece);
            boolean existsValidMove = IntStream.range(0, validMoves.length)
                    .boxed()
                    .flatMap(col -> IntStream.range(0, validMoves[col].length)
                            .filter(row -> validMoves[col][row])
                            .mapToObj(row -> 1))
                    .anyMatch(count -> count > 0);

            if (existsValidMove) {
                return false;
            }
        }
        return true;
    }

    private boolean[][] findValidMove(PlayBoardDTO playBoard, PieceDTO pieceDTO) {
        boolean[][] validMoves = new boolean[MAX_COL][MAX_ROW];
        for (int toCol = 1; toCol <= MAX_COL; toCol++) {
            for (int toRow = 1; toRow <= MAX_ROW; toRow++) {
                validMoves[toCol - 1][toRow - 1] = movingRuleService.isMoveValid(playBoard, pieceDTO, toCol, toRow);
            }
        }
        return validMoves;
    }

}
