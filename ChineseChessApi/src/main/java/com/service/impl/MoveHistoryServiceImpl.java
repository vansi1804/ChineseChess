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

import com.common.ErrorMessage;
import com.common.enumeration.EPiece;
import com.data.dto.move.MoveCreationDTO;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.data.dto.move.MoveCreationResponseDTO;
import com.data.dto.move.BestMoveDTO;
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
import com.service.MoveHistoryService;
import com.service.MoveRuleService;
import com.service.MoveDescriptionService;
import com.service.PlayBoardService;
import com.service.PieceService;

@Service
public class MoveHistoryServiceImpl implements MoveHistoryService {

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
    public MoveHistoryServiceImpl(
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
                    int toCol = mh.getToCol();
                    int toRow = mh.getToRow();

                    long turn = mh.getTurn();
                    PieceDTO movedPieceDTO = pieceService.findOneInBoard(currentBoardDTO.get(), mh.getPiece().getId());
                    String description = moveDescriptionService.build(
                            currentBoardDTO.get(), movedPieceDTO, toCol, toRow);

                    MoveCreationResponseDTO movedResponseDTO = buildMovedResponse(
                            currentBoardDTO.get(), movedPieceDTO, toCol, toRow);

                    MoveHistoryDTO moveHistoryDTO = new MoveHistoryDTO();
                    moveHistoryDTO.setLastDeadPieceDTOs(lastDeadPieceDTOs.get());
                    moveHistoryDTO.setTurn(turn);
                    moveHistoryDTO.setMovedPieceDTO(movedResponseDTO.getMovedPieceDTO());
                    moveHistoryDTO.setDescription(description);
                    moveHistoryDTO.setDeadPieceDTO(movedResponseDTO.getDeadPieceDTO());
                    moveHistoryDTO.setCurrentBoardDTO(movedResponseDTO.getCurrentBoardDTO());
                    moveHistoryDTO.setCheckedGeneralPieceDTO(movedResponseDTO.getCheckedGeneralPieceDTO());
                    moveHistoryDTO.setCheckMate(movedResponseDTO.isCheckMate());

                    // update board after moved
                    currentBoardDTO.set(movedResponseDTO.getCurrentBoardDTO());
                    // add deadPiece in this turn to list for reusing in next turn
                    if (movedResponseDTO.getDeadPieceDTO() != null) {
                        lastDeadPieceDTOs.get().add(movedResponseDTO.getDeadPieceDTO());
                    }

                    playBoardService.printTest(
                            moveHistoryDTO.getTurn() + ":\t" + moveHistoryDTO.getDescription(),
                            moveHistoryDTO.getCurrentBoardDTO(),
                            moveHistoryDTO.getMovedPieceDTO());

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

        return buildMovedResponse(
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

            MoveCreationResponseDTO movedResponseDTO = buildMovedResponse(
                    currentBoard, movingPieceDTO, trainingMoveCreationDTO.getToCol(),
                    trainingMoveCreationDTO.getToRow());

            playBoardService.printTest("Turn: " + newTurn, movedResponseDTO.getCurrentBoardDTO(), movingPieceDTO);

            return movedResponseDTO;
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

            MoveCreationResponseDTO movedResponseDTO = buildMovedResponse(
                    currentBoard, movingPieceDTO, matchMoveCreationDTO.getToCol(), matchMoveCreationDTO.getToRow());

            playBoardService.printTest("Turn: " + newTurn, movedResponseDTO.getCurrentBoardDTO(), movingPieceDTO);

            return movedResponseDTO;
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
    public List<BestMoveDTO> findAllBestMoves(PlayBoardDTO playBoardDTO, int depth) {

        List<PieceDTO> pieceDTOsInBoard = pieceService.findAllInBoard(playBoardDTO, null, null);
        
        
        return null;
    }

    private MoveCreationResponseDTO buildMovedResponse(
            PlayBoardDTO playBoardDTO, PieceDTO movingPieceDTO, int toCol, int toRow) {

        PieceDTO deadPieceDTO = playBoardDTO.getState()[toCol][toRow];
        PlayBoardDTO updatedPlayBoardDTO = playBoardService.update(playBoardDTO, movingPieceDTO, toCol, toRow);
        PieceDTO checkedGeneralPieceDTO = findGeneralBeingChecked(playBoardDTO, !movingPieceDTO.isRed());
        boolean isCheckMate = isCheckMateState(updatedPlayBoardDTO, movingPieceDTO.isRed());

        MoveCreationResponseDTO movedResponseDTO = new MoveCreationResponseDTO();
        movedResponseDTO.setMovedPieceDTO(movingPieceDTO);
        movedResponseDTO.setToCol(toCol);
        movedResponseDTO.setToRow(toRow);
        movedResponseDTO.setDeadPieceDTO(deadPieceDTO);
        movedResponseDTO.setCurrentBoardDTO(updatedPlayBoardDTO);
        movedResponseDTO.setCheckedGeneralPieceDTO(checkedGeneralPieceDTO);
        movedResponseDTO.setCheckMate(isCheckMate);

        return movedResponseDTO;
    }

    private PieceDTO findGeneralBeingChecked(PlayBoardDTO playBoardDTO, boolean isRed) {
        // check opponent general is being check after moving
        List<PieceDTO> generals = pieceService.findAllInBoard(playBoardDTO, EPiece.GENERAL.name(), isRed);
        PieceDTO general = generals.isEmpty() ? null : generals.get(0);

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
