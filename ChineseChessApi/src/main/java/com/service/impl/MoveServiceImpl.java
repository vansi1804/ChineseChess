package com.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.ErrorMessage;
import com.data.dto.move.MoveCreationDTO;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.data.dto.move.MoveDTO;
import com.data.dto.move.BestAvailableMoveRequestDTO;
import com.data.dto.move.BestMoveResponseDTO;
import com.data.dto.move.MatchMoveCreationDTO;
import com.data.dto.move.MoveHistoryDTO;
import com.data.dto.move.TrainingMoveCreationDTO;
import com.data.dto.move.AvailableMoveRequestDTO;
import com.data.entity.Match;
import com.data.entity.MoveHistory;
import com.data.entity.Training;
import com.data.mapper.MoveHistoryMapper;
import com.data.repository.MatchRepository;
import com.data.repository.MoveHistoryRepository;
import com.data.repository.TrainingRepository;
import com.config.exception.InvalidExceptionCustomize;
import com.config.exception.ResourceNotFoundExceptionCustomize;
import com.service.MoveService;
import com.service.MoveRuleService;
import com.service.MoveDescriptionService;
import com.service.PlayBoardService;
import com.service.PieceService;

@Service
public class MoveServiceImpl implements MoveService {

    private final MoveHistoryRepository moveHistoryRepository;
    private final MoveHistoryMapper moveHistoryMapper;
    private final TrainingRepository trainingRepository;
    private final MatchRepository matchRepository;
    private final PlayBoardService playBoardService;
    private final MoveDescriptionService moveDescriptionService;
    private final PieceService pieceService;
    private final MoveRuleService moveRuleService;

    @Autowired
    public MoveServiceImpl(
            MoveHistoryRepository moveHistoryRepository,
            MoveHistoryMapper moveHistoryMapper,
            TrainingRepository trainingRepository,
            MatchRepository matchRepository,
            PlayBoardService playBoardService,
            MoveDescriptionService moveDescriptionService,
            PieceService pieceService,
            MoveRuleService moveRuleService) {

        this.moveHistoryRepository = moveHistoryRepository;
        this.moveHistoryMapper = moveHistoryMapper;
        this.trainingRepository = trainingRepository;
        this.matchRepository = matchRepository;
        this.playBoardService = playBoardService;
        this.moveDescriptionService = moveDescriptionService;
        this.pieceService = pieceService;
        this.moveRuleService = moveRuleService;
    }

    @Override
    public Map<Long, MoveHistoryDTO> build(List<MoveHistory> moveHistories) {
        AtomicReference<PlayBoardDTO> playBoardDTO = new AtomicReference<>(playBoardService.generate());

        playBoardService.printTest("start: ", playBoardDTO.get(), null);

        AtomicReference<List<PieceDTO>> lastDeadPieceDTOs = new AtomicReference<>(new ArrayList<>());

        return moveHistories.stream()
                .map(mh -> {
                    long turn = mh.getTurn();
                    PieceDTO movingPieceDTO = pieceService.findOneInBoard(playBoardDTO.get(), mh.getPiece().getId());
                    String description = moveDescriptionService.build(
                            playBoardDTO.get(), movingPieceDTO, mh.getToCol(), mh.getToRow());

                    System.out.println("\nTurn " + String.valueOf(turn) + ": \t\t" + description);

                    MoveDTO moveDTO = buildMoveCreationResponse(
                            playBoardDTO.get(), movingPieceDTO, mh.getToCol(), mh.getToRow());

                    MoveHistoryDTO moveHistoryDTO = new MoveHistoryDTO();
                    moveHistoryDTO.setLastDeadPieceDTOs(lastDeadPieceDTOs.get());
                    moveHistoryDTO.setTurn(turn);
                    moveHistoryDTO.setMovingPieceDTO(moveDTO.getMovingPieceDTO());
                    moveHistoryDTO.setToCol(moveDTO.getToCol());
                    moveHistoryDTO.setToRow(moveDTO.getToRow());
                    moveHistoryDTO.setDescription(description);
                    moveHistoryDTO.setDeadPieceDTO(moveDTO.getDeadPieceDTO());
                    moveHistoryDTO.setPlayBoardDTO(moveDTO.getPlayBoardDTO());
                    moveHistoryDTO.setCheckedGeneralPieceDTO(moveDTO.getCheckedGeneralPieceDTO());
                    moveHistoryDTO.setCheckMate(moveDTO.isCheckMate());

                    // update board after moved for reusing in next turn
                    playBoardDTO.set(moveDTO.getPlayBoardDTO());
                    // add deadPiece in this turn to list for reusing in next turn
                    List<PieceDTO> tempLastDeadPieceDTOs = new ArrayList<>(lastDeadPieceDTOs.get());
                    if (moveDTO.getDeadPieceDTO() != null) {
                        tempLastDeadPieceDTOs.add(moveDTO.getDeadPieceDTO());
                    }
                    lastDeadPieceDTOs.set(tempLastDeadPieceDTOs);

                    return moveHistoryDTO;
                })
                .collect(Collectors.toMap(MoveHistoryDTO::getTurn, Function.identity()));
    }

    @Override
    public List<int[]> findAllAvailable(AvailableMoveRequestDTO availableMoveRequestDTO) {
        PieceDTO movingPieceDTO = pieceService.findOneInBoard(
                availableMoveRequestDTO.getPlayBoardDTO(), availableMoveRequestDTO.getMovingPieceId());

        if (movingPieceDTO == null) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("message", ErrorMessage.DEAD_PIECE);
            errors.put("movingPieceId", availableMoveRequestDTO.getMovingPieceId());

            throw new InvalidExceptionCustomize(errors);
        }

        List<int[]> availableMoveIndexes = findAllAvailableMoveIndexes(
                availableMoveRequestDTO.getPlayBoardDTO(), movingPieceDTO);

        playBoardService.printTest(availableMoveRequestDTO.getPlayBoardDTO(), movingPieceDTO, availableMoveIndexes);

        return availableMoveIndexes;
    }

    @Override
    public MoveDTO create(MoveCreationDTO moveCreationDTO) {
        PieceDTO movingPieceDTO = pieceService.findOneInBoard(
                moveCreationDTO.getPlayBoardDTO(), moveCreationDTO.getMovingPieceId());

        if (movingPieceDTO == null) {
            throw new InvalidExceptionCustomize(
                    ErrorMessage.DEAD_PIECE,
                    Collections.singletonMap("movingPieceId", moveCreationDTO.getMovingPieceId()));
        }

        boolean isAvailableMove = isAvailableMove(
                moveCreationDTO.getPlayBoardDTO(), movingPieceDTO, moveCreationDTO.getToCol(),
                moveCreationDTO.getToRow());

        if (isAvailableMove) {
            return buildMoveCreationResponse(
                    moveCreationDTO.getPlayBoardDTO(), movingPieceDTO,
                    moveCreationDTO.getToCol(), moveCreationDTO.getToRow());
        } else {
            Map<String, Object> errors = new HashMap<>();
            errors.put("message", ErrorMessage.INVALID_MOVE);
            errors.put("movingPieceDTO", movingPieceDTO);
            errors.put("toCol", moveCreationDTO.getToCol());
            errors.put("toRow", moveCreationDTO.getToRow());

            throw new InvalidExceptionCustomize(errors);
        }
    }

    @Override
    public MoveDTO create(TrainingMoveCreationDTO trainingMoveCreationDTO) {
        Training training = trainingRepository.findById(trainingMoveCreationDTO.getTrainingId())
                .orElseThrow(
                        () -> new ResourceNotFoundExceptionCustomize(
                                Collections.singletonMap("trainingId", trainingMoveCreationDTO.getTrainingId())));

        long newTurn = moveHistoryRepository.countTurnByTraining_Id(training.getId()) + 1;

        List<MoveHistory> moveHistories = moveHistoryRepository.findAllByTraining_Id(training.getId());
        PlayBoardDTO playBoardDTO = playBoardService.build(moveHistories);

        // check piece input existing in playBoardDTO
        PieceDTO movingPieceDTO = pieceService.findOneInBoard(playBoardDTO, trainingMoveCreationDTO.getMovingPieceId());
        if (movingPieceDTO == null) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("message", ErrorMessage.DEAD_PIECE);
            errors.put("trainingId", training.getId());
            errors.put("turn", newTurn);
            errors.put("movingPieceId", trainingMoveCreationDTO.getMovingPieceId());

            throw new InvalidExceptionCustomize(errors);
        }

        if (((newTurn % 2 != 0) && !movingPieceDTO.isRed())
                || ((newTurn % 2 == 0) && movingPieceDTO.isRed())) {

            Map<String, Object> errors = new HashMap<>();
            errors.put("message", ErrorMessage.OPPONENT_TURN);
            errors.put("trainingId", training.getId());
            errors.put("turn", newTurn);
            errors.put("movingPieceDTO", movingPieceDTO);

            throw new InvalidExceptionCustomize(errors);
        }

        boolean isAvailableMove = isAvailableMove(
                playBoardDTO, movingPieceDTO, trainingMoveCreationDTO.getToCol(), trainingMoveCreationDTO.getToRow());

        if (isAvailableMove) {
            MoveHistory moveHistory = moveHistoryMapper.toEntity(trainingMoveCreationDTO);
            moveHistory.setTurn(newTurn);
            moveHistoryRepository.save(moveHistory);

            return buildMoveCreationResponse(
                    playBoardDTO, movingPieceDTO,
                    trainingMoveCreationDTO.getToCol(), trainingMoveCreationDTO.getToRow());
        } else {
            Map<String, Object> errors = new HashMap<>();
            errors.put("message", ErrorMessage.INVALID_MOVE);
            errors.put("trainingId", training.getId());
            errors.put("turn", newTurn);
            errors.put("movingPieceDTO", movingPieceDTO);
            errors.put("toCol", trainingMoveCreationDTO.getToCol());
            errors.put("toRow", trainingMoveCreationDTO.getToRow());

            throw new InvalidExceptionCustomize(errors);
        }
    }

    @Override
    public MoveDTO create(MatchMoveCreationDTO matchMoveCreationDTO) {
        Match match = matchRepository.findById(matchMoveCreationDTO.getMatchId())
                .orElseThrow(
                        () -> new ResourceNotFoundExceptionCustomize(
                                Collections.singletonMap("matchId", matchMoveCreationDTO.getMatchId())));

        if (match.getResult() != null) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("message", ErrorMessage.END_MATCH);
            errors.put("matchId", match.getId());

            throw new InvalidExceptionCustomize(errors);
        }

        if ((match.getPlayer1().getId() != matchMoveCreationDTO.getPlayerId())
                && (match.getPlayer2().getId() != matchMoveCreationDTO.getPlayerId())) {

            Map<String, Object> errors = new HashMap<>();
            errors.put("message", ErrorMessage.INVALID_MOVING_PLAYER);
            errors.put("playerId", matchMoveCreationDTO.getPlayerId());

            throw new InvalidExceptionCustomize(errors);
        }

        long newTurn = moveHistoryRepository.countTurnByMatch_Id(match.getId()) + 1;

        List<MoveHistory> moveHistories = moveHistoryRepository.findAllByMatch_Id(match.getId());
        PlayBoardDTO playBoardDTO = playBoardService.build(moveHistories);

        // check piece input existing in playBoardDTO
        PieceDTO movingPieceDTO = pieceService.findOneInBoard(playBoardDTO, matchMoveCreationDTO.getMovingPieceId());
        if (movingPieceDTO == null) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("message", ErrorMessage.DEAD_PIECE);
            errors.put("matchId", match.getId());
            errors.put("turn", newTurn);
            errors.put("movingPieceId", matchMoveCreationDTO.getMovingPieceId());

            throw new InvalidExceptionCustomize(errors);
        }

        // check color turn
        if (((newTurn % 2 != 0) && !movingPieceDTO.isRed())
                || ((newTurn % 2 == 0) && movingPieceDTO.isRed())) {

            Map<String, Object> errors = new HashMap<>();
            errors.put("message", ErrorMessage.OPPONENT_TURN);
            errors.put("matchId", match.getId());
            errors.put("turn", newTurn);
            errors.put("movingPieceDTO", movingPieceDTO);

            throw new InvalidExceptionCustomize(errors);
        }

        // check player move valid piece
        if (movingPieceDTO.isRed() && (match.getPlayer1().getId() != matchMoveCreationDTO.getPlayerId())) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("message", ErrorMessage.INVALID_PLAYER_MOVE_PIECE);
            errors.put("matchId", match.getId());
            errors.put("playerId", matchMoveCreationDTO.getMovingPieceId());
            errors.put("turn", newTurn);
            errors.put("movingPieceDTO", movingPieceDTO);

            throw new InvalidExceptionCustomize(errors);
        }

        boolean isAvailableMove = isAvailableMove(
                playBoardDTO, movingPieceDTO, matchMoveCreationDTO.getToCol(), matchMoveCreationDTO.getToRow());

        if (isAvailableMove) {
            MoveHistory moveHistory = moveHistoryMapper.toEntity(matchMoveCreationDTO);
            moveHistory.setTurn(newTurn);
            moveHistoryRepository.save(moveHistory);

            return buildMoveCreationResponse(
                    playBoardDTO, movingPieceDTO, matchMoveCreationDTO.getToCol(), matchMoveCreationDTO.getToRow());
        } else {
            Map<String, Object> errors = new HashMap<>();
            errors.put("matchId", match.getId());
            errors.put("message", ErrorMessage.INVALID_MOVE);
            errors.put("turn", newTurn);
            errors.put("movingPieceDTO", movingPieceDTO);
            errors.put("toCol", matchMoveCreationDTO.getToCol());
            errors.put("toRow", matchMoveCreationDTO.getToRow());

            throw new InvalidExceptionCustomize(errors);
        }
    }

    @Override
    public List<BestMoveResponseDTO> findAllBestAvailable(BestAvailableMoveRequestDTO bestAvailableMoveRequestDTO) {
        List<PieceDTO> pieceDTOsInBoard = pieceService.findAllInBoard(
                bestAvailableMoveRequestDTO.getPlayBoardDTO(), null, bestAvailableMoveRequestDTO.getIsRed());

        int maxScore = Integer.MIN_VALUE;

        List<BestMoveResponseDTO> bestMoves = new ArrayList<>();

        for (PieceDTO pieceDTO : pieceDTOsInBoard) {
            List<int[]> availableMoveIndexes = findAllAvailableMoveIndexes(
                    bestAvailableMoveRequestDTO.getPlayBoardDTO(), pieceDTO);

            for (int[] index : availableMoveIndexes) {
                PlayBoardDTO updatedBoard = playBoardService.update(
                        bestAvailableMoveRequestDTO.getPlayBoardDTO(), pieceDTO, index[0], index[1]);

                // Flip isRed for the opponent
                int score = minimax(updatedBoard, !pieceDTO.isRed(), bestAvailableMoveRequestDTO.getDepth() - 1);

                if (score >= maxScore) {
                    BestMoveResponseDTO bestMove = new BestMoveResponseDTO();
                    bestMove.setPieceDTO(pieceDTO);
                    bestMove.setBestAvailableMoveIndexes(availableMoveIndexes);
                    bestMove.setScore(score);

                    if (score > maxScore) {
                        bestMoves.clear();
                        maxScore = score;
                    }

                    bestMoves.add(bestMove);
                }
            }
        }

        return bestMoves;
    }

    private MoveDTO buildMoveCreationResponse(
            PlayBoardDTO playBoardDTO, PieceDTO movingPieceDTO, int toCol, int toRow) {

        PieceDTO deadPieceDTO = playBoardDTO.getState()[toCol][toRow];
        PlayBoardDTO updatedPlayBoardDTO = playBoardService.update(playBoardDTO, movingPieceDTO, toCol, toRow);
        PieceDTO checkedGeneralPieceDTO = findGeneralBeingChecked(playBoardDTO, !movingPieceDTO.isRed());
        boolean isCheckMate = isCheckMateState(updatedPlayBoardDTO, movingPieceDTO.isRed());

        MoveDTO moveDTO = new MoveDTO();
        moveDTO.setMovingPieceDTO(movingPieceDTO);
        moveDTO.setToCol(toCol);
        moveDTO.setToRow(toRow);
        moveDTO.setDeadPieceDTO(deadPieceDTO);
        moveDTO.setPlayBoardDTO(updatedPlayBoardDTO);
        moveDTO.setCheckedGeneralPieceDTO(checkedGeneralPieceDTO);
        moveDTO.setCheckMate(isCheckMate);

        playBoardService.printTest("", moveDTO.getPlayBoardDTO(), movingPieceDTO);

        return moveDTO;
    }

    private PieceDTO findGeneralBeingChecked(PlayBoardDTO playBoardDTO, boolean isRed) {
        PieceDTO generalPieceDTO = pieceService.findGeneralInBoard(playBoardDTO, isRed);
        return playBoardService.isGeneralBeingChecked(playBoardDTO, generalPieceDTO) ? generalPieceDTO : null;
    }

    private List<int[]> findAllAvailableMoveIndexes(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO) {
        int fromCol = 0;
        int fromRow = 0;
        int toCol = playBoardDTO.getState().length - 1;
        int toRow = playBoardDTO.getState()[0].length - 1;

        return IntStream.rangeClosed(fromCol, toCol)
                .boxed()
                .flatMap(col -> IntStream.rangeClosed(fromRow, toRow)
                        .filter(row -> isAvailableMove(playBoardDTO, pieceDTO, col, row))
                        .mapToObj(row -> new int[] { col, row }))
                .collect(Collectors.toList());
    }

    private boolean isCheckMateState(PlayBoardDTO playBoardDTO, boolean isRed) {
        List<PieceDTO> opponentPiecesInBoard = pieceService.findAllInBoard(playBoardDTO, null, !isRed);

        int fromCol = 0;
        int fromRow = 0;
        int toCol = playBoardDTO.getState().length - 1;
        int toRow = playBoardDTO.getState()[0].length - 1;

        return opponentPiecesInBoard.stream()
                .flatMap(opponentPiece -> IntStream.rangeClosed(fromCol, toCol)
                        .boxed()
                        .flatMap(col -> IntStream.rangeClosed(fromRow, toRow)
                                .mapToObj(row -> new int[] { col, row })
                                .filter(index -> isAvailableMove(playBoardDTO, opponentPiece, index[0], index[1]))
                                .findFirst()
                                .stream()))
                .findAny()
                .isEmpty();
    }

    private boolean isCheckMateState(PlayBoardDTO playBoardDTO) {
        return isCheckMateState(playBoardDTO, true) || isCheckMateState(playBoardDTO, false);
    }

    private int minimax(PlayBoardDTO playBoardDTO, boolean isRed, int depth) {
        // break when depth == 0 or board is in checkmate state
        if ((depth == 0) || isCheckMateState(playBoardDTO)) {
            return evaluatePlayBoard(playBoardDTO);
        }

        if (isRed) {
            int maxScore = Integer.MIN_VALUE;
            List<PieceDTO> sameColorPieceDTOsInBoard = pieceService.findAllInBoard(playBoardDTO, null, isRed);

            for (PieceDTO pieceDTO : sameColorPieceDTOsInBoard) {
                List<int[]> availableMoveIndexes = findAllAvailableMoveIndexes(playBoardDTO, pieceDTO);
                for (int[] index : availableMoveIndexes) {
                    int toCol = index[0];
                    int toRow = index[1];
                    PlayBoardDTO updatedBoard = playBoardService.update(playBoardDTO, pieceDTO, toCol, toRow);
                    int eval = minimax(updatedBoard, !pieceDTO.isRed(), depth - 1);
                    maxScore = Math.max(maxScore, eval);
                }
            }

            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            List<PieceDTO> opponentPieceDTOsInBoard = pieceService.findAllInBoard(playBoardDTO, null, !isRed);

            for (PieceDTO pieceDTO : opponentPieceDTOsInBoard) {
                List<int[]> availableMoveIndexes = findAllAvailableMoveIndexes(playBoardDTO, pieceDTO);
                for (int[] index : availableMoveIndexes) {
                    int toCol = index[0];
                    int toRow = index[1];
                    PlayBoardDTO updatedBoard = playBoardService.update(playBoardDTO, pieceDTO, toCol, toRow);
                    int eval = minimax(updatedBoard, !pieceDTO.isRed(), depth - 1);
                    minScore = Math.min(minScore, eval);
                }
            }

            return minScore;
        }
    }

    private int evaluatePlayBoard(PlayBoardDTO playBoardDTO) {
        int fromCol = 0;
        int fromRow = 0;
        int toCol = playBoardDTO.getState().length - 1;
        int toRow = playBoardDTO.getState()[0].length - 1;

        return IntStream.rangeClosed(fromCol, toCol)
                .flatMap(col -> IntStream.rangeClosed(fromRow, toRow)
                        .filter(row -> playBoardDTO.getState()[col][row] != null)
                        .map(row -> {
                            PieceDTO pieceDTO = playBoardDTO.getState()[col][row];
                            int piecePower = pieceService.convertByName(pieceDTO.getName()).getPower();

                            return pieceDTO.isRed() ? piecePower : -piecePower;
                        }))
                .sum();
    }

    private boolean isAvailableMove(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, int toCol, int toRow) {
        // available move is valid move and the same color general is safe after move
        boolean isValidMove = moveRuleService.isValid(playBoardDTO, pieceDTO, toCol, toRow);
        if (isValidMove) {
            PieceDTO generalPieceDTO = pieceService.findGeneralInBoard(playBoardDTO, pieceDTO.isRed());
            PlayBoardDTO updatedPlayBoardDTO = playBoardService.update(playBoardDTO, pieceDTO, toCol, toRow);

            boolean isGeneralInSafe = playBoardService.isGeneralInSafe(updatedPlayBoardDTO, generalPieceDTO);

            return isGeneralInSafe;
        } else {
            return false;
        }
    }

}
