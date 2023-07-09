package com.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.ErrorMessage;
import com.common.enumeration.EPiece;
import com.data.dto.MovedResponseDTO;
import com.data.dto.MatchMoveCreationDTO;
import com.data.dto.MoveHistoryDTO;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.data.dto.TrainingMoveCreationDTO;
import com.data.dto.ValidMoveRequestDTO;
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
    public MoveHistoryServiceImpl(MoveHistoryRepository moveHistoryRepository,
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
        Map<Long, MoveHistoryDTO> movedHistoriesDTOs = new HashMap<>();

        AtomicReference<PlayBoardDTO> currentBoard = new AtomicReference<>(playBoardService.generate());

        /* print test */
        System.out.println("start:");
        currentBoard.get().print(null);
        /* =================================== */

        moveHistories.forEach(mh -> {
            PieceDTO movingPieceDTO = pieceService.findOneInBoard(currentBoard.get(), mh.getPiece().getId());

            MoveHistoryDTO moveHistoryDTO = new MoveHistoryDTO();
            moveHistoryDTO.setTurn(mh.getTurn());
            moveHistoryDTO.setMovingPieceDTO(movingPieceDTO);
            moveHistoryDTO.setDescription(
                    moveDescriptionService.build(
                            currentBoard.get(), movingPieceDTO, mh.getToCol(), mh.getToRow()));
            moveHistoryDTO.setDeadPieceDTO(currentBoard.get().getState()[mh.getToCol() - 1][mh.getToRow() - 1]);

            currentBoard.set(playBoardService.update(
                    currentBoard.get(), movingPieceDTO, mh.getToCol(), mh.getToRow()));

            moveHistoryDTO.setCurrentBoard(currentBoard.get());
            moveHistoryDTO.setGeneralBeingChecked(
                    findGeneralBeingChecked(currentBoard.get(), !movingPieceDTO.isRed()));

            movedHistoriesDTOs.put(moveHistoryDTO.getTurn(), moveHistoryDTO);
            
            /* print test */
            System.out.println("\n" + moveHistoryDTO.getTurn() + ":\t" + moveHistoryDTO.getDescription());
            moveHistoryDTO.getCurrentBoard().print(moveHistoryDTO.getMovingPieceDTO());
            /* =================================== */
        });

        return movedHistoriesDTOs;
    }

    @Override
    public List<int[]> findMoveValid(ValidMoveRequestDTO validMoveRequestDTO) {
        PieceDTO movingPieceDTO = pieceRepository.findById(validMoveRequestDTO.getPieceId())
                .map(p -> pieceMapper.toDTO(p))
                .orElseThrow(() -> new ResourceNotFoundException(
                        Collections.singletonMap("pieceId", validMoveRequestDTO.getPieceId())));

        PieceDTO foundPieceInBoard = pieceService.findOneInBoard(
                validMoveRequestDTO.getCurrentBoard(), movingPieceDTO.getId());
        if (foundPieceInBoard == null) {
            throw new InvalidException(ErrorMessage.DEAD_PIECE,
                    Collections.singletonMap("pieceId", validMoveRequestDTO.getPieceId()));
        } else {
            movingPieceDTO.setCurrentCol(foundPieceInBoard.getCurrentCol());
            movingPieceDTO.setCurrentRow(foundPieceInBoard.getCurrentRow());
        }

        return findValidMove(validMoveRequestDTO.getCurrentBoard(), movingPieceDTO);
    }

    @Override
    public MovedResponseDTO create(TrainingMoveCreationDTO trainingMoveCreationDTO) {
        Training training = trainingRepository.findById(trainingMoveCreationDTO.getTrainingId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Collections.singletonMap("trainingId", trainingMoveCreationDTO.getTrainingId())));

        long newTurn = moveHistoryRepository.countTurnByTraining_Id(training.getId()) + 1;

        PieceDTO movingPieceDTO = pieceRepository.findById(trainingMoveCreationDTO.getPieceId())
                .map(p -> pieceMapper.toDTO(p))
                .orElseThrow(() -> new ResourceNotFoundException(
                        Collections.singletonMap("pieceId", trainingMoveCreationDTO.getPieceId())));

        PlayBoardDTO currentBoard = playBoardService.buildPlayBoardByMoveHistories(
                moveHistoryRepository.findAllByTraining_Id(training.getId()));

        // check piece input existing in currentBoard
        PieceDTO foundPieceInBoard = pieceService.findOneInBoard(currentBoard, movingPieceDTO.getId());
        if (foundPieceInBoard == null) {
            int[] lastPosition = moveHistoryRepository.findLastPositionByTrainingIdAndPieceId(
                    training.getId(), movingPieceDTO.getId());
            if (lastPosition != null) {
                movingPieceDTO.setCurrentCol(lastPosition[0]);
                movingPieceDTO.setCurrentRow(lastPosition[1]);
            }

            Map<String, Object> errors = new HashMap<>();
            errors.put("trainingId", training.getId());
            errors.put("turn", newTurn);
            errors.put("pieceDTO", movingPieceDTO);
            throw new InvalidException(ErrorMessage.DEAD_PIECE, errors);
        } else {
            movingPieceDTO.setCurrentCol(foundPieceInBoard.getCurrentCol());
            movingPieceDTO.setCurrentRow(foundPieceInBoard.getCurrentRow());
        }

        if (((newTurn % 2 != 0) && !movingPieceDTO.isRed()
                || (newTurn % 2 == 0) && movingPieceDTO.isRed())) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("trainingId", training.getId());
            errors.put("turn", newTurn);
            errors.put("pieceDTO", movingPieceDTO);
            throw new InvalidException(ErrorMessage.OPPONENT_TURN, errors);
        }

        /* print test */
        System.out.println("current");
        currentBoard.print(movingPieceDTO);
        /* =================================== */

        boolean isValidMove = moveRuleService.isMoveValid(currentBoard, movingPieceDTO,
                trainingMoveCreationDTO.getToCol(), trainingMoveCreationDTO.getToRow());
        if (isValidMove) {
            MoveHistory moveHistory = moveHistoryMapper.toEntity(trainingMoveCreationDTO);
            moveHistory.setTurn(newTurn);
            moveHistoryRepository.save(moveHistory);

            MovedResponseDTO movedResponseDTO = create(
                    currentBoard, movingPieceDTO, trainingMoveCreationDTO.getToCol(),
                    trainingMoveCreationDTO.getToRow());

            /* print test */
            System.out.println("===========================================");
            System.out.println("Turn: " + newTurn);
            currentBoard.print(movingPieceDTO);
            /* =================================== */

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
    public MovedResponseDTO create(MatchMoveCreationDTO matchMoveCreationDTO) {
        Match match = matchRepository.findById(matchMoveCreationDTO.getMatchId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        Collections.singletonMap("matchId", matchMoveCreationDTO.getMatchId())));

        if (match.getResult() != null) {
            throw new InvalidException(ErrorMessage.END_MATCH,
                    Collections.singletonMap("matchId", match.getId()));
        }

        if ((match.getPlayer1().getId() != matchMoveCreationDTO.getPlayerId())
                && (match.getPlayer2().getId() != matchMoveCreationDTO.getPlayerId())) {
            throw new InvalidException(ErrorMessage.INVALID_MOVING_PLAYER,
                    Collections.singletonMap("playerId", matchMoveCreationDTO.getPlayerId()));
        }

        long newTurn = moveHistoryRepository.countTurnByMatch_Id(match.getId()) + 1;

        PieceDTO movingPieceDTO = pieceRepository.findById(matchMoveCreationDTO.getPieceId())
                .map(p -> pieceMapper.toDTO(p))
                .orElseThrow(() -> new ResourceNotFoundException(
                        Collections.singletonMap("pieceId", matchMoveCreationDTO.getPieceId())));

        PlayBoardDTO currentBoard = playBoardService.buildPlayBoardByMoveHistories(
                moveHistoryRepository.findAllByMatch_Id(match.getId()));

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

        /* print test */
        System.out.println("current");
        currentBoard.print(movingPieceDTO);
        /* =================================== */

        boolean isValidMove = moveRuleService.isMoveValid(currentBoard, movingPieceDTO,
                matchMoveCreationDTO.getToCol(), matchMoveCreationDTO.getToRow());
        if (isValidMove) {
            MoveHistory moveHistory = moveHistoryMapper.toEntity(matchMoveCreationDTO);
            moveHistory.setTurn(newTurn);
            moveHistoryRepository.save(moveHistory);

            MovedResponseDTO movedResponseDTO = create(
                    currentBoard, movingPieceDTO, matchMoveCreationDTO.getToCol(), matchMoveCreationDTO.getToRow());

            if (movedResponseDTO.isCheckMate()) {
                match.setResult(matchMoveCreationDTO.getPlayerId());
                matchRepository.save(match);
            }

            /* print test */
            System.out.println("===========================================");
            System.out.println("Turn" + newTurn);
            currentBoard.print(movingPieceDTO);
            /* =================================== */

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

    private PieceDTO findGeneralBeingChecked(PlayBoardDTO playBoard, boolean isRed) {
        // check opponent general is being check after moving
        List<PieceDTO> generals = pieceService.findAllInBoard(playBoard, EPiece.General.getFullName(), isRed);
        PieceDTO general = generals.isEmpty() ? null : generals.get(0);

        return moveRuleService.isGeneralBeingChecked(playBoard, general) ? general : null;
    }

    private List<int[]> findValidMove(PlayBoardDTO playBoard, PieceDTO pieceDTO) {
        int col = playBoard.getState().length;
        int row = playBoard.getState()[0].length;
        List<int[]> validMoves = new ArrayList<>();

        for (int toCol = 1; toCol <= col; toCol++) {
            for (int toRow = 1; toRow <= row; toRow++) {
                boolean validMove = moveRuleService.isMoveValid(playBoard, pieceDTO, toCol, toRow);
                if (validMove) {
                    int[] index = new int[2];
                    index[0] = toCol;
                    index[1] = toRow;
                    validMoves.add(index);
                }
            }
        }
        return validMoves;
    }

    private boolean isCheckMateState(PlayBoardDTO playBoard, boolean isRed) {
        List<PieceDTO> opponentPiecesInBoard = pieceService.findAllInBoard(playBoard, null, !isRed);
        int col = playBoard.getState().length;
        int row = playBoard.getState()[0].length;

        for (PieceDTO opponentPiece : opponentPiecesInBoard) {
            boolean isValidMove = false;
            for (int toCol = 1; toCol <= col; toCol++) {
                for (int toRow = 1; toRow <= row; toRow++) {
                    isValidMove = moveRuleService.isMoveValid(
                            playBoard, opponentPiece, toCol, toRow);
                }
            }
            if (isValidMove) {
                return false;
            }
        }
        return true;
    }

    private MovedResponseDTO create(
            PlayBoardDTO playBoard, PieceDTO movingPieceDTO, int toCol, int toRow) {

        MovedResponseDTO movedResponseDTO = new MovedResponseDTO();

        movedResponseDTO.setDeadPieceDTO(playBoard.getState()[toCol - 1][toCol - 1]);
        playBoard = playBoardService.update(playBoard, movingPieceDTO, toCol, toRow);
        movedResponseDTO.setGeneralBeingChecked(findGeneralBeingChecked(playBoard, !movingPieceDTO.isRed()));
        movedResponseDTO.setCheckMate(isCheckMateState(playBoard, movingPieceDTO.isRed()));

        return movedResponseDTO;
    }

}
