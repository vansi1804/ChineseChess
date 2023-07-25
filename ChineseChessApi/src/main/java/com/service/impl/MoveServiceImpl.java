package com.service.impl;

import java.util.AbstractMap;
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

import com.common.Default;
import com.common.ErrorMessage;
import com.common.enumeration.EPiece;
import com.data.dto.move.MoveCreationDTO;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.data.dto.move.MoveCreationResponseDTO;
import com.data.dto.move.BestMoveRequestDTO;
import com.data.dto.move.BestMoveResponseDTO;
import com.data.dto.move.MatchMoveCreationDTO;
import com.data.dto.move.MoveHistoryDTO;
import com.data.dto.move.TrainingMoveCreationDTO;
import com.data.dto.move.ValidMoveRequestDTO;
import com.data.entity.Match;
import com.data.entity.MoveHistory;
import com.data.entity.Training;
import com.data.mapper.MoveHistoryMapper;
import com.data.mapper.PieceMapper;
import com.data.repository.MatchRepository;
import com.data.repository.MoveHistoryRepository;
import com.data.repository.TrainingRepository;
import com.data.repository.PieceRepository;
import com.config.exception.InvalidException;
import com.config.exception.ResourceNotFoundException;
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
    private final PieceRepository pieceRepository;
    private final PieceMapper pieceMapper;
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
            PieceRepository pieceRepository,
            PieceMapper pieceMapper,
            PieceService pieceService,
            MoveRuleService moveRuleService) {

        this.moveHistoryRepository = moveHistoryRepository;
        this.moveHistoryMapper = moveHistoryMapper;
        this.trainingRepository = trainingRepository;
        this.matchRepository = matchRepository;
        this.playBoardService = playBoardService;
        this.moveDescriptionService = moveDescriptionService;
        this.pieceRepository = pieceRepository;
        this.pieceMapper = pieceMapper;
        this.pieceService = pieceService;
        this.moveRuleService = moveRuleService;
    }

    @Override
    public Map<Long, MoveHistoryDTO> build(List<MoveHistory> moveHistories) {
        AtomicReference<PlayBoardDTO> currentBoardDTO = new AtomicReference<>(playBoardService.generate());

        playBoardService.printTest("start: ", currentBoardDTO.get(), null);

        AtomicReference<List<PieceDTO>> lastDeadPieceDTOs = new AtomicReference<>(new ArrayList<>());

        return moveHistories.stream()
                .map(mh -> {
                    long turn = mh.getTurn();
                    PieceDTO movingPieceDTO = pieceService.findOneInBoard(currentBoardDTO.get(), mh.getPiece().getId());
                    String description = moveDescriptionService.build(
                            currentBoardDTO.get(), movingPieceDTO, mh.getToCol(), mh.getToRow());

                    MoveCreationResponseDTO moveCreationResponseDTO = buildMoveCreationResponse(
                            currentBoardDTO.get(), movingPieceDTO, mh.getToCol(), mh.getToRow());

                    MoveHistoryDTO moveHistoryDTO = new MoveHistoryDTO();
                    moveHistoryDTO.setLastDeadPieceDTOs(lastDeadPieceDTOs.get());
                    moveHistoryDTO.setTurn(turn);
                    moveHistoryDTO.setMovingPieceDTO(moveCreationResponseDTO.getMovingPieceDTO());
                    moveHistoryDTO.setToCol(moveCreationResponseDTO.getToCol());
                    moveHistoryDTO.setToRow(moveCreationResponseDTO.getToRow());
                    moveHistoryDTO.setDescription(description);
                    moveHistoryDTO.setDeadPieceDTO(moveCreationResponseDTO.getDeadPieceDTO());
                    moveHistoryDTO.setCurrentBoardDTO(moveCreationResponseDTO.getCurrentBoardDTO());
                    moveHistoryDTO.setCheckedGeneralPieceDTO(moveCreationResponseDTO.getCheckedGeneralPieceDTO());
                    moveHistoryDTO.setCheckMate(moveCreationResponseDTO.isCheckMate());

                    // update board after moved for reusing in next turn
                    currentBoardDTO.set(moveCreationResponseDTO.getCurrentBoardDTO());
                    // add deadPiece in this turn to list for reusing in next turn
                    List<PieceDTO> tempLastDeadPieceDTOs = new ArrayList<>(lastDeadPieceDTOs.get());
                    if (moveCreationResponseDTO.getDeadPieceDTO() != null) {
                        tempLastDeadPieceDTOs.add(moveCreationResponseDTO.getDeadPieceDTO());
                    }
                    lastDeadPieceDTOs.set(tempLastDeadPieceDTOs);

                    playBoardService.printTest(
                            moveHistoryDTO.getTurn() + ":\t" + moveHistoryDTO.getDescription(),
                            moveHistoryDTO.getCurrentBoardDTO(),
                            moveHistoryDTO.getMovingPieceDTO());

                    return moveHistoryDTO;
                })
                .collect(Collectors.toMap(MoveHistoryDTO::getTurn, Function.identity()));
    }

    @Override
    public List<int[]> findAllAvailableMoves(ValidMoveRequestDTO validMoveRequestDTO) {
        PieceDTO movingPieceDTO = pieceRepository.findById(validMoveRequestDTO.getPieceId())
                .map(p -> pieceMapper.toDTO(p))
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                Collections.singletonMap("pieceId", validMoveRequestDTO.getPieceId())));

        PieceDTO foundPieceInBoard = pieceService.findOneInBoard(
                validMoveRequestDTO.getCurrentBoardDTO(), movingPieceDTO.getId());

        if (foundPieceInBoard == null) {
            throw new InvalidException(
                    ErrorMessage.DEAD_PIECE,
                    Collections.singletonMap("pieceId", validMoveRequestDTO.getPieceId()));
        } else {
            movingPieceDTO.setCurrentCol(foundPieceInBoard.getCurrentCol());
            movingPieceDTO.setCurrentRow(foundPieceInBoard.getCurrentRow());
        }

        List<int[]> availableMoveIndexes = findAllAvailableMoveIndexes(
                validMoveRequestDTO.getCurrentBoardDTO(), movingPieceDTO);

        playBoardService.printTest(validMoveRequestDTO.getCurrentBoardDTO(), movingPieceDTO, availableMoveIndexes);

        return availableMoveIndexes;
    }

    @Override
    public MoveCreationResponseDTO create(MoveCreationDTO moveCreationDTO) {
        if (!pieceRepository.existsById(moveCreationDTO.getPieceId())) {
            throw new ResourceNotFoundException(
                    Collections.singletonMap("pieceId", moveCreationDTO.getPieceId()));
        }

        PieceDTO movingPieceDTO = pieceService.findOneInBoard(
                moveCreationDTO.getCurrentBoardDTO(), moveCreationDTO.getPieceId());

        if (movingPieceDTO == null) {
            throw new InvalidException(
                    ErrorMessage.DEAD_PIECE,
                    Collections.singletonMap("pieceId", moveCreationDTO.getPieceId()));
        }

        return buildMoveCreationResponse(
                moveCreationDTO.getCurrentBoardDTO(), movingPieceDTO,
                moveCreationDTO.getToCol(), moveCreationDTO.getToRow());
    }

    @Override
    public MoveCreationResponseDTO create(TrainingMoveCreationDTO trainingMoveCreationDTO) {
        Training training = trainingRepository.findById(trainingMoveCreationDTO.getTrainingId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                Collections.singletonMap("trainingId", trainingMoveCreationDTO.getTrainingId())));

        long newTurn = moveHistoryRepository.countTurnByTraining_Id(training.getId()) + 1;

        if (!pieceRepository.existsById(trainingMoveCreationDTO.getPieceId())) {
            throw new ResourceNotFoundException(
                    Collections.singletonMap("pieceId", trainingMoveCreationDTO.getPieceId()));
        }

        PlayBoardDTO currentBoard = playBoardService.buildPlayBoardByMoveHistories(
                moveHistoryRepository.findAllByTraining_Id(training.getId()));

        // check piece input existing in currentBoard
        PieceDTO movingPieceDTO = pieceService.findOneInBoard(currentBoard, trainingMoveCreationDTO.getPieceId());
        if (movingPieceDTO == null) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("trainingId", training.getId());
            errors.put("turn", newTurn);
            errors.put("pieceId", trainingMoveCreationDTO.getPieceId());
            throw new InvalidException(ErrorMessage.DEAD_PIECE, errors);
        }

        if (((newTurn % 2 != 0) && !movingPieceDTO.isRed()
                || (newTurn % 2 == 0) && movingPieceDTO.isRed())) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("trainingId", training.getId());
            errors.put("turn", newTurn);
            errors.put("pieceDTO", movingPieceDTO);
            throw new InvalidException(ErrorMessage.OPPONENT_TURN, errors);
        }

        boolean isAvailableMove = moveRuleService.isAvailableMove(
                currentBoard, movingPieceDTO,
                trainingMoveCreationDTO.getToCol(), trainingMoveCreationDTO.getToRow());

        if (isAvailableMove) {
            MoveHistory moveHistory = moveHistoryMapper.toEntity(trainingMoveCreationDTO);
            moveHistory.setTurn(newTurn);
            moveHistoryRepository.save(moveHistory);

            MoveCreationResponseDTO moveCreationResponseDTO = buildMoveCreationResponse(
                    currentBoard, movingPieceDTO, trainingMoveCreationDTO.getToCol(),
                    trainingMoveCreationDTO.getToRow());

            playBoardService.printTest("Turn: " + newTurn, moveCreationResponseDTO.getCurrentBoardDTO(),
                    movingPieceDTO);

            return moveCreationResponseDTO;
        }

        Map<String, Object> errors = new HashMap<>();
        errors.put("trainingId", training.getId());
        errors.put("turn", newTurn);
        errors.put("pieceDTO", movingPieceDTO);
        errors.put("toCol", trainingMoveCreationDTO.getToCol());
        errors.put("toRow", trainingMoveCreationDTO.getToRow());
        throw new InvalidException(ErrorMessage.INVALID_MOVE, errors);
    }

    @Override
    public MoveCreationResponseDTO create(MatchMoveCreationDTO matchMoveCreationDTO) {
        Match match = matchRepository.findById(matchMoveCreationDTO.getMatchId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                Collections.singletonMap("matchId", matchMoveCreationDTO.getMatchId())));

        if (match.getResult() != null) {
            throw new InvalidException(
                    ErrorMessage.END_MATCH,
                    Collections.singletonMap("matchId", match.getId()));
        }

        if ((match.getPlayer1().getId() != matchMoveCreationDTO.getPlayerId())
                && (match.getPlayer2().getId() != matchMoveCreationDTO.getPlayerId())) {
            throw new InvalidException(
                    ErrorMessage.INVALID_MOVING_PLAYER,
                    Collections.singletonMap("playerId", matchMoveCreationDTO.getPlayerId()));
        }

        long newTurn = moveHistoryRepository.countTurnByMatch_Id(match.getId()) + 1;

        if (!pieceRepository.existsById(matchMoveCreationDTO.getPieceId())) {
            throw new ResourceNotFoundException(
                    Collections.singletonMap("pieceId", matchMoveCreationDTO.getPieceId()));
        }

        PlayBoardDTO currentBoard = playBoardService.buildPlayBoardByMoveHistories(
                moveHistoryRepository.findAllByMatch_Id(match.getId()));

        // check piece input existing in currentBoard
        PieceDTO movingPieceDTO = pieceService.findOneInBoard(currentBoard, matchMoveCreationDTO.getPieceId());
        if (movingPieceDTO == null) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("matchId", match.getId());
            errors.put("turn", newTurn);
            errors.put("pieceId", matchMoveCreationDTO.getPieceId());
            throw new InvalidException(ErrorMessage.DEAD_PIECE, errors);
        }

        // check color turn
        if (((newTurn % 2 != 0) && !movingPieceDTO.isRed()
                || (newTurn % 2 == 0) && movingPieceDTO.isRed())) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("matchId", match.getId());
            errors.put("turn", newTurn);
            errors.put("pieceDTO", movingPieceDTO);
            throw new InvalidException(ErrorMessage.OPPONENT_TURN, errors);
        }

        // check player move valid piece
        if (movingPieceDTO.isRed() && (match.getPlayer1().getId() != matchMoveCreationDTO.getPlayerId())) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("matchId", match.getId());
            errors.put("playerId", matchMoveCreationDTO.getPieceId());
            errors.put("turn", newTurn);
            errors.put("pieceDTO", movingPieceDTO);
            throw new InvalidException(ErrorMessage.INVALID_PLAYER_MOVE_PIECE, errors);
        }

        boolean isAvailableMove = moveRuleService.isAvailableMove(currentBoard, movingPieceDTO,
                matchMoveCreationDTO.getToCol(), matchMoveCreationDTO.getToRow());
        if (isAvailableMove) {
            MoveHistory moveHistory = moveHistoryMapper.toEntity(matchMoveCreationDTO);
            moveHistory.setTurn(newTurn);
            moveHistoryRepository.save(moveHistory);

            MoveCreationResponseDTO moveCreationResponseDTO = buildMoveCreationResponse(
                    currentBoard, movingPieceDTO, matchMoveCreationDTO.getToCol(), matchMoveCreationDTO.getToRow());

            playBoardService.printTest("Turn: " + newTurn, moveCreationResponseDTO.getCurrentBoardDTO(),
                    movingPieceDTO);

            return moveCreationResponseDTO;
        }

        Map<String, Object> errors = new HashMap<>();
        errors.put("matchId", match.getId());
        errors.put("turn", newTurn);
        errors.put("pieceDTO", movingPieceDTO);
        errors.put("toCol", matchMoveCreationDTO.getToCol());
        errors.put("toRow", matchMoveCreationDTO.getToRow());
        throw new InvalidException(ErrorMessage.INVALID_MOVE, errors);
    }

    @Override
    public List<BestMoveResponseDTO> findAllBestMoves(BestMoveRequestDTO bestMoveRequestDTO) {
        List<PieceDTO> pieceDTOsInBoard = pieceService.findAllInBoard(
                bestMoveRequestDTO.getPlayBoardDTO(), null, bestMoveRequestDTO.getIsRed());

        List<BestMoveResponseDTO> bestMoves = new ArrayList<>();

        for (PieceDTO pieceDTO : pieceDTOsInBoard) {
            List<int[]> availableMoveIndexes = findAllAvailableMoveIndexes(
                    bestMoveRequestDTO.getPlayBoardDTO(), pieceDTO);

            for (int[] index : availableMoveIndexes) {
                PlayBoardDTO updatedBoard = playBoardService.update(
                        bestMoveRequestDTO.getPlayBoardDTO(), pieceDTO, index[0], index[1]);

                // Flip isRed for the opponent
                int score = minimax(updatedBoard, !pieceDTO.isRed(), bestMoveRequestDTO.getDepth() - 1);

                BestMoveResponseDTO bestMove = new BestMoveResponseDTO();
                bestMove.setPieceDTO(pieceDTO);
                bestMove.setBestAvailableMoveIndexes(availableMoveIndexes);
                bestMove.setScore(score);

                bestMoves.add(bestMove);
            }
        }

        return bestMoves;
    }

    private int minimax(PlayBoardDTO playBoardDTO, boolean isRed, int depth) {
        // break when depth == 0 or (red or black is in checkmate state)
        if (depth == 0 || isCheckMateState(playBoardDTO, true) || isCheckMateState(playBoardDTO, false)) {
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
                    System.out.println("depth: " + (depth - 1));
                    int eval = minimax(updatedBoard, !isRed, depth - 1);
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
                    System.out.println("depth: " + (depth - 1));
                    int eval = minimax(updatedBoard, isRed, depth - 1);
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

    private MoveCreationResponseDTO buildMoveCreationResponse(
            PlayBoardDTO playBoardDTO, PieceDTO movingPieceDTO, int toCol, int toRow) {

        PieceDTO deadPieceDTO = playBoardDTO.getState()[toCol][toRow];
        PlayBoardDTO updatedPlayBoardDTO = playBoardService.update(playBoardDTO, movingPieceDTO, toCol, toRow);
        PieceDTO checkedGeneralPieceDTO = findGeneralBeingChecked(playBoardDTO, !movingPieceDTO.isRed());
        boolean isCheckMate = isCheckMateState(updatedPlayBoardDTO, movingPieceDTO.isRed());

        MoveCreationResponseDTO moveCreationResponseDTO = new MoveCreationResponseDTO();
        moveCreationResponseDTO.setMovingPieceDTO(movingPieceDTO);
        moveCreationResponseDTO.setToCol(toCol);
        moveCreationResponseDTO.setToRow(toRow);
        moveCreationResponseDTO.setDeadPieceDTO(deadPieceDTO);
        moveCreationResponseDTO.setCurrentBoardDTO(updatedPlayBoardDTO);
        moveCreationResponseDTO.setCheckedGeneralPieceDTO(checkedGeneralPieceDTO);
        moveCreationResponseDTO.setCheckMate(isCheckMate);

        return moveCreationResponseDTO;
    }

    private PieceDTO findGeneralBeingChecked(PlayBoardDTO playBoardDTO, boolean isRed) {
        final int CENTER_COL_INDEX_MIN = Default.Game.PlayBoardSize.CENTER_COL_MIN - 1;
        final int CENTER_COL_INDEX_MAX = Default.Game.PlayBoardSize.CENTER_COL_MAX - 1;
        final int CENTER_ROW_INDEX_MIN = (isRed ? Default.Game.PlayBoardSize.RedArea.CENTER_ROW_MIN
                : Default.Game.PlayBoardSize.BlackArea.CENTER_ROW_MIN) - 1;
        final int CENTER_ROW_INDEX_MAX = (isRed ? Default.Game.PlayBoardSize.RedArea.CENTER_ROW_MAX
                : Default.Game.PlayBoardSize.BlackArea.CENTER_ROW_MAX) - 1;

        // check opponent general is being check after moving
        PieceDTO general = pieceService.findAllInBoard(
                playBoardDTO, EPiece.GENERAL.name(), isRed,
                CENTER_COL_INDEX_MIN, CENTER_ROW_INDEX_MIN, CENTER_COL_INDEX_MAX, CENTER_ROW_INDEX_MAX)
                .stream()
                .findFirst()
                .get();

        return moveRuleService.isGeneralBeingChecked(playBoardDTO, general) ? general : null;
    }

    private List<int[]> findAllAvailableMoveIndexes(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO) {
        int fromCol = 0;
        int fromRow = 0;
        int toCol = playBoardDTO.getState().length - 1;
        int toRow = playBoardDTO.getState()[0].length - 1;

        return IntStream.rangeClosed(fromCol, toCol)
                .boxed()
                .flatMap(col -> IntStream.rangeClosed(fromRow, toRow)
                        .filter(row -> moveRuleService.isAvailableMove(playBoardDTO, pieceDTO, col, row))
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
                .flatMap(opponentPiece -> {
                    return IntStream.rangeClosed(fromCol, toCol)
                            .boxed()
                            .flatMap(col -> IntStream.rangeClosed(fromRow, toRow)
                                    .boxed()
                                    .map(row -> new AbstractMap.SimpleEntry<>(col, row))
                                    .filter(entry -> moveRuleService.isAvailableMove(
                                            playBoardDTO, opponentPiece, entry.getKey(), entry.getValue()))
                                    .findFirst()
                                    .stream());
                })
                .findAny()
                .isEmpty();
    }

}
