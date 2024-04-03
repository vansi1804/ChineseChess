package com.service.impl;

import com.common.ErrorMessage;
import com.config.exception.InvalidExceptionCustomize;
import com.config.exception.ResourceNotFoundExceptionCustomize;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.data.dto.move.MoveCreationDTO;
import com.data.dto.move.MoveDTO;
import com.data.dto.move.MoveHistoryDTO;
import com.data.dto.move.availableMove.AvailableMoveRequestDTO;
import com.data.dto.move.availableMove.bestAvailableMove.BestAvailableMoveRequestDTO;
import com.data.dto.move.availableMove.bestAvailableMove.BestAvailableMoveResponseDTO;
import com.data.dto.move.availableMove.bestAvailableMove.BestMoveResponseDTO;
import com.data.dto.move.matchMove.MatchMoveCreationDTO;
import com.data.dto.move.trainingMove.TrainingMoveCreationDTO;
import com.data.entity.Match;
import com.data.entity.MoveHistory;
import com.data.entity.Training;
import com.data.mapper.MoveHistoryMapper;
import com.data.repository.MatchRepository;
import com.data.repository.MoveHistoryRepository;
import com.data.repository.TrainingRepository;
import com.service.MoveDescriptionService;
import com.service.MoveRuleService;
import com.service.MoveService;
import com.service.PieceService;
import com.service.PlayBoardService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoveServiceImpl implements MoveService {

  private final MoveHistoryRepository moveHistoryRepository;
  private final MoveHistoryMapper moveHistoryMapper;
  private final TrainingRepository trainingRepository;
  private final MatchRepository matchRepository;
  private final PlayBoardService playBoardService;
  private final MoveDescriptionService moveDescriptionService;
  private final PieceService pieceService;
  private final MoveRuleService moveRuleService;

  @Override
  public Map<Long, MoveHistoryDTO> findAllByMatchId(long matchId) {
    if (!matchRepository.existsById(matchId)) {
      throw new ResourceNotFoundExceptionCustomize(
        Collections.singletonMap("matchId", matchId)
      );
    }
    return build(moveHistoryRepository.findAllByMatch_Id(matchId));
  }

  @Override
  public Map<Long, MoveHistoryDTO> findAllByTrainingId(long trainingId) {
    if (!trainingRepository.existsById(trainingId)) {
      throw new ResourceNotFoundExceptionCustomize(
        Collections.singletonMap("trainingId", trainingId)
      );
    }
    return build(moveHistoryRepository.findAllByTraining_Id(trainingId));
  }

  @Override
  public List<int[]> findAllAvailable(
    AvailableMoveRequestDTO availableMoveRequestDTO
  ) {
    PieceDTO movingPieceDTO = pieceService.findOneInBoard(
      availableMoveRequestDTO.getPlayBoardDTO(),
      availableMoveRequestDTO.getMovingPieceId()
    );

    if (movingPieceDTO == null) {
      Map<String, Object> errors = new HashMap<>();
      errors.put("message", ErrorMessage.DEAD_PIECE);
      errors.put("movingPieceId", availableMoveRequestDTO.getMovingPieceId());

      throw new InvalidExceptionCustomize(errors);
    }

    List<int[]> availableMoveIndexes =
      this.findAllAvailableMoveIndexes(
          availableMoveRequestDTO.getPlayBoardDTO(),
          movingPieceDTO
        );

    playBoardService.printTest(
      availableMoveRequestDTO.getPlayBoardDTO(),
      movingPieceDTO,
      availableMoveIndexes
    );

    return availableMoveIndexes;
  }

  @Override
  public MoveDTO create(MoveCreationDTO moveCreationDTO) {
    PieceDTO movingPieceDTO = pieceService.findOneInBoard(
      moveCreationDTO.getPlayBoardDTO(),
      moveCreationDTO.getMovingPieceId()
    );

    if (movingPieceDTO == null) {
      throw new InvalidExceptionCustomize(
        ErrorMessage.DEAD_PIECE,
        Collections.singletonMap(
          "movingPieceId",
          moveCreationDTO.getMovingPieceId()
        )
      );
    }

    boolean isAvailableMove =
      this.isAvailableMove(
          moveCreationDTO.getPlayBoardDTO(),
          movingPieceDTO,
          moveCreationDTO.getToCol(),
          moveCreationDTO.getToRow()
        );

    if (isAvailableMove) {
      return this.buildMoveCreationResponse(
          moveCreationDTO.getPlayBoardDTO(),
          movingPieceDTO,
          moveCreationDTO.getToCol(),
          moveCreationDTO.getToRow()
        );
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
    Training training = trainingRepository
      .findById(trainingMoveCreationDTO.getTrainingId())
      .orElseThrow(() ->
        new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap(
            "trainingId",
            trainingMoveCreationDTO.getTrainingId()
          )
        )
      );

    long newTurn =
      moveHistoryRepository.countTurnByTraining_Id(training.getId()) + 1;

    List<MoveHistory> moveHistories = moveHistoryRepository.findAllByTraining_Id(
      training.getId()
    );
    PlayBoardDTO playBoardDTO = playBoardService.build(moveHistories);

    // check piece input existing in playBoardDTO
    PieceDTO movingPieceDTO = pieceService.findOneInBoard(
      playBoardDTO,
      trainingMoveCreationDTO.getMovingPieceId()
    );
    if (movingPieceDTO == null) {
      Map<String, Object> errors = new HashMap<>();
      errors.put("message", ErrorMessage.DEAD_PIECE);
      errors.put("trainingId", training.getId());
      errors.put("turn", newTurn);
      errors.put("movingPieceId", trainingMoveCreationDTO.getMovingPieceId());

      throw new InvalidExceptionCustomize(errors);
    }

    if (
      ((newTurn % 2 != 0) && !movingPieceDTO.isRed()) ||
      ((newTurn % 2 == 0) && movingPieceDTO.isRed())
    ) {
      Map<String, Object> errors = new HashMap<>();
      errors.put("message", ErrorMessage.OPPONENT_TURN);
      errors.put("trainingId", training.getId());
      errors.put("turn", newTurn);
      errors.put("movingPieceDTO", movingPieceDTO);

      throw new InvalidExceptionCustomize(errors);
    }

    boolean isAvailableMove =
      this.isAvailableMove(
          playBoardDTO,
          movingPieceDTO,
          trainingMoveCreationDTO.getToCol(),
          trainingMoveCreationDTO.getToRow()
        );

    if (isAvailableMove) {
      MoveHistory moveHistory = moveHistoryMapper.toEntity(
        trainingMoveCreationDTO
      );
      moveHistory.setTurn(newTurn);
      moveHistoryRepository.save(moveHistory);

      return this.buildMoveCreationResponse(
          playBoardDTO,
          movingPieceDTO,
          trainingMoveCreationDTO.getToCol(),
          trainingMoveCreationDTO.getToRow()
        );
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
    Match match = matchRepository
      .findById(matchMoveCreationDTO.getMatchId())
      .orElseThrow(() ->
        new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("matchId", matchMoveCreationDTO.getMatchId())
        )
      );

    if (match.getResult() != null) {
      Map<String, Object> errors = new HashMap<>();
      errors.put("message", ErrorMessage.END_MATCH);
      errors.put("matchId", match.getId());

      throw new InvalidExceptionCustomize(errors);
    }

    if (
      (match.getPlayer1().getId() != matchMoveCreationDTO.getPlayerId()) &&
      (match.getPlayer2().getId() != matchMoveCreationDTO.getPlayerId())
    ) {
      Map<String, Object> errors = new HashMap<>();
      errors.put("message", ErrorMessage.INVALID_MOVING_PLAYER);
      errors.put("playerId", matchMoveCreationDTO.getPlayerId());

      throw new InvalidExceptionCustomize(errors);
    }

    long newTurn = moveHistoryRepository.countTurnByMatch_Id(match.getId()) + 1;

    List<MoveHistory> moveHistories = moveHistoryRepository.findAllByMatch_Id(
      match.getId()
    );
    PlayBoardDTO playBoardDTO = playBoardService.build(moveHistories);

    // check piece input existing in playBoardDTO
    PieceDTO movingPieceDTO = pieceService.findOneInBoard(
      playBoardDTO,
      matchMoveCreationDTO.getMovingPieceId()
    );
    if (movingPieceDTO == null) {
      Map<String, Object> errors = new HashMap<>();
      errors.put("message", ErrorMessage.DEAD_PIECE);
      errors.put("matchId", match.getId());
      errors.put("turn", newTurn);
      errors.put("movingPieceId", matchMoveCreationDTO.getMovingPieceId());

      throw new InvalidExceptionCustomize(errors);
    }

    // check piece turn
    if (
      ((newTurn % 2 != 0) && !movingPieceDTO.isRed()) ||
      ((newTurn % 2 == 0) && movingPieceDTO.isRed())
    ) {
      Map<String, Object> errors = new HashMap<>();
      errors.put("message", ErrorMessage.OPPONENT_TURN);
      errors.put("matchId", match.getId());
      errors.put("turn", newTurn);
      errors.put("movingPieceDTO", movingPieceDTO);

      throw new InvalidExceptionCustomize(errors);
    }

    // check player move valid piece
    if (
      movingPieceDTO.isRed() &&
      Objects.equals(
        match.getPlayer1().getId(),
        matchMoveCreationDTO.getPlayerId()
      )
    ) {
      Map<String, Object> errors = new HashMap<>();
      errors.put("message", ErrorMessage.INVALID_PLAYER_MOVE_PIECE);
      errors.put("matchId", match.getId());
      errors.put("playerId", matchMoveCreationDTO.getMovingPieceId());
      errors.put("turn", newTurn);
      errors.put("movingPieceDTO", movingPieceDTO);

      throw new InvalidExceptionCustomize(errors);
    }

    boolean isAvailableMove =
      this.isAvailableMove(
          playBoardDTO,
          movingPieceDTO,
          matchMoveCreationDTO.getToCol(),
          matchMoveCreationDTO.getToRow()
        );

    if (isAvailableMove) {
      MoveHistory moveHistory = moveHistoryMapper.toEntity(
        matchMoveCreationDTO
      );
      moveHistory.setTurn(newTurn);

      moveHistoryRepository.save(moveHistory);

      return this.buildMoveCreationResponse(
          playBoardDTO,
          movingPieceDTO,
          matchMoveCreationDTO.getToCol(),
          matchMoveCreationDTO.getToRow()
        );
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
  private MoveDTO buildMoveCreationResponse(
    PlayBoardDTO playBoardDTO,
    PieceDTO movingPieceDTO,
    int toCol,
    int toRow
  ) {
    MoveDTO moveDTO = new MoveDTO();
    moveDTO.setMovingPieceDTO(movingPieceDTO);
    moveDTO.setToCol(toCol);
    moveDTO.setToRow(toRow);

    PieceDTO deadPieceDTO = playBoardDTO.getState()[toCol][toRow];
    moveDTO.setDeadPieceDTO(deadPieceDTO);

    PlayBoardDTO updatedPlayBoardDTO = playBoardService.update(
      playBoardDTO,
      movingPieceDTO,
      toCol,
      toRow
    );
    moveDTO.setPlayBoardDTO(updatedPlayBoardDTO);

    // find opponent general being checked
    PieceDTO opponentGeneralPieceDTO = pieceService.findGeneralInBoard(
      playBoardDTO,
      !movingPieceDTO.isRed()
    );
    PieceDTO checkedGeneralPieceDTO = playBoardService.isGeneralBeingChecked(
        playBoardDTO,
        opponentGeneralPieceDTO
      )
      ? opponentGeneralPieceDTO
      : null;
    moveDTO.setCheckedGeneralPieceDTO(checkedGeneralPieceDTO);

    moveDTO.setCheckmateState(
      this.isCheckmateState(updatedPlayBoardDTO, movingPieceDTO.isRed())
    );

    playBoardService.printTest("", moveDTO.getPlayBoardDTO(), movingPieceDTO);

    return moveDTO;
  }

  private List<int[]> findAllAvailableMoveIndexes(
    PlayBoardDTO playBoardDTO,
    PieceDTO pieceDTO
  ) {
    int fromCol = 0;
    int fromRow = 0;
    int toCol = playBoardDTO.getState().length - 1;
    int toRow = playBoardDTO.getState()[0].length - 1;

    return IntStream
      .rangeClosed(fromCol, toCol)
      .boxed()
      .flatMap(col ->
        IntStream
          .rangeClosed(fromRow, toRow)
          .filter(row -> this.isAvailableMove(playBoardDTO, pieceDTO, col, row))
          .mapToObj(row -> new int[] { col, row })
      )
      .collect(Collectors.toList());
  }

  private boolean isCheckmateState(PlayBoardDTO playBoardDTO, boolean isRed) {
    int fromCol = 0;
    int fromRow = 0;
    int toCol = playBoardDTO.getState().length - 1;
    int toRow = playBoardDTO.getState()[0].length - 1;
    List<PieceDTO> opponentPiecesInBoard = pieceService.findAllInBoard(
      playBoardDTO,
      null,
      !isRed
    );

    return opponentPiecesInBoard
      .stream()
      .flatMap(opponentPiece ->
        IntStream
          .rangeClosed(fromCol, toCol)
          .boxed()
          .flatMap(col ->
            IntStream
              .rangeClosed(fromRow, toRow)
              .mapToObj(row -> new int[] { col, row })
              .filter(index ->
                this.isAvailableMove(
                    playBoardDTO,
                    opponentPiece,
                    index[0],
                    index[1]
                  )
              )
              .findFirst()
              .stream()
          )
      )
      .findAny()
      .isEmpty();
  }

  private boolean isAvailableMove(
    PlayBoardDTO playBoardDTO,
    PieceDTO pieceDTO,
    int toCol,
    int toRow
  ) {
    // available move is valid move and the same color general is safe after move
    boolean isValidMove = moveRuleService.isValid(
      playBoardDTO,
      pieceDTO,
      toCol,
      toRow
    );
    if (isValidMove) {
      PlayBoardDTO updatedPlayBoardDTO = playBoardService.update(
        playBoardDTO,
        pieceDTO,
        toCol,
        toRow
      );
      PieceDTO generalPieceDTO = pieceService.findGeneralInBoard(
        updatedPlayBoardDTO,
        pieceDTO.isRed()
      );

      return playBoardService.isGeneralInSafe(
        updatedPlayBoardDTO,
        generalPieceDTO
      );
    } else {
      return false;
    }
  }

  @Override
  public Map<Boolean, BestAvailableMoveResponseDTO> findAllBestAvailable(
      BestAvailableMoveRequestDTO bestAvailableMoveRequestDTO) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findAllBestAvailable'");
  }

}
