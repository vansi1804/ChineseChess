package com.nvs.service.impl;

import com.nvs.config.exception.InvalidExceptionCustomize;
import com.nvs.config.exception.ResourceNotFoundExceptionCustomize;
import com.nvs.config.i18nMessage.Translator;
import com.nvs.data.dto.PieceDTO;
import com.nvs.data.dto.PlayBoardDTO;
import com.nvs.data.dto.move.MoveCreationDTO;
import com.nvs.data.dto.move.MoveDTO;
import com.nvs.data.dto.move.MoveHistoryDTO;
import com.nvs.data.dto.move.availableMove.AvailableMoveRequestDTO;
import com.nvs.data.dto.move.availableMove.bestAvailableMove.BestAvailableMoveRequestDTO;
import com.nvs.data.dto.move.availableMove.bestAvailableMove.BestAvailableMoveResponseDTO;
import com.nvs.data.dto.move.availableMove.bestAvailableMove.BestMoveResponseDTO;
import com.nvs.data.dto.move.matchMove.MatchMoveCreationDTO;
import com.nvs.data.dto.move.trainingMove.TrainingMoveCreationDTO;
import com.nvs.data.entity.Match;
import com.nvs.data.entity.MoveHistory;
import com.nvs.data.entity.Training;
import com.nvs.data.mapper.MoveHistoryMapper;
import com.nvs.data.repository.MatchRepository;
import com.nvs.data.repository.MoveHistoryRepository;
import com.nvs.data.repository.TrainingRepository;
import com.nvs.service.MoveDescriptionService;
import com.nvs.service.MoveRuleService;
import com.nvs.service.MoveService;
import com.nvs.service.PieceService;
import com.nvs.service.PlayBoardService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MoveServiceImpl implements MoveService {

  private static final Map<PlayBoardDTO, Integer> transpositionTable = new HashMap<>();
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
      log.error("Match with ID {} not found", matchId);
      throw new ResourceNotFoundExceptionCustomize(Collections.singletonMap("matchId", matchId));
    }
    log.info("Finding all move histories for match ID {}", matchId);
    return this.build(moveHistoryRepository.findAllByMatch_Id(matchId));
  }

  @Override
  public Map<Long, MoveHistoryDTO> findAllByTrainingId(long trainingId) {
    if (!trainingRepository.existsById(trainingId)) {
      log.error("Training with ID {} not found", trainingId);
      throw new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("trainingId", trainingId));
    }
    log.info("Finding all move histories for training ID {}", trainingId);
    return this.build(moveHistoryRepository.findAllByTraining_Id(trainingId));
  }


  @Override
  public List<int[]> findAllAvailable(AvailableMoveRequestDTO availableMoveRequestDTO) {
    PieceDTO movingPieceDTO = pieceService.findOneInBoard(availableMoveRequestDTO.getPlayBoardDTO(),
        availableMoveRequestDTO.getMovingPieceId());

    if (movingPieceDTO == null) {
      log.error("Piece with ID {} not found on the board",
          availableMoveRequestDTO.getMovingPieceId());
      Map<String, Object> errors = new HashMap<>();
      errors.put("message", Translator.toLocale("NOT_FOUND_WITH"));
      errors.put("movingPieceId", availableMoveRequestDTO.getMovingPieceId());
      throw new InvalidExceptionCustomize(errors);
    }

    List<int[]> availableMoveIndexes = this.findAllAvailableMoveIndexes(
        availableMoveRequestDTO.getPlayBoardDTO(), movingPieceDTO);

    log.debug("Available moves for piece ID {}: {}", availableMoveRequestDTO.getMovingPieceId(),
        availableMoveIndexes);

    playBoardService.printTest(availableMoveRequestDTO.getPlayBoardDTO(), movingPieceDTO,
        availableMoveIndexes);

    return availableMoveIndexes;
  }

  @Override
  public MoveDTO create(MoveCreationDTO moveCreationDTO) {
    PieceDTO movingPieceDTO = pieceService.findOneInBoard(moveCreationDTO.getPlayBoardDTO(),
        moveCreationDTO.getMovingPieceId());

    if (movingPieceDTO == null) {
      log.error("Attempted to create move with dead piece ID {}",
          moveCreationDTO.getMovingPieceId());
      throw new InvalidExceptionCustomize(Translator.toLocale("DEAD_PIECE"),
          Collections.singletonMap("movingPieceId", moveCreationDTO.getMovingPieceId()));
    }

    boolean isAvailableMove = this.isAvailableMove(moveCreationDTO.getPlayBoardDTO(),
        movingPieceDTO, moveCreationDTO.getToCol(), moveCreationDTO.getToRow());

    if (isAvailableMove) {
      log.info("Creating move for piece ID {} to position ({}, {})",
          moveCreationDTO.getMovingPieceId(),
          moveCreationDTO.getToCol(), moveCreationDTO.getToRow());
      return this.makeMove(moveCreationDTO.getPlayBoardDTO(), movingPieceDTO,
          moveCreationDTO.getToCol(), moveCreationDTO.getToRow());
    } else {
      log.warn("Invalid move attempt for piece ID {} to position ({}, {})",
          moveCreationDTO.getMovingPieceId(),
          moveCreationDTO.getToCol(), moveCreationDTO.getToRow());
      Map<String, Object> errors = new HashMap<>();
      errors.put("message", Translator.toLocale("INVALID_MOVE"));
      errors.put("movingPieceDTO", movingPieceDTO);
      errors.put("toCol", moveCreationDTO.getToCol());
      errors.put("toRow", moveCreationDTO.getToRow());
      throw new InvalidExceptionCustomize(errors);
    }
  }

  @Override
  public MoveDTO create(TrainingMoveCreationDTO trainingMoveCreationDTO) {
    Training training = trainingRepository.findById(trainingMoveCreationDTO.getTrainingId())
        .orElseThrow(() -> new ResourceNotFoundExceptionCustomize(
            Collections.singletonMap("trainingId", trainingMoveCreationDTO.getTrainingId())));

    long newTurn = moveHistoryRepository.countTurnByTraining_Id(training.getId()) + 1;

    log.info("Creating training move for training ID {} on turn {}", training.getId(), newTurn);

    List<MoveHistory> moveHistories = moveHistoryRepository.findAllByTraining_Id(training.getId());
    PlayBoardDTO playBoardDTO = playBoardService.build(moveHistories);

    PieceDTO movingPieceDTO = pieceService.findOneInBoard(playBoardDTO,
        trainingMoveCreationDTO.getMovingPieceId());

    if (movingPieceDTO == null) {
      log.error("Training move creation attempted with dead piece ID {}",
          trainingMoveCreationDTO.getMovingPieceId());
      Map<String, Object> errors = new HashMap<>();
      errors.put("message", Translator.toLocale("DEAD_PIECE"));
      errors.put("trainingId", training.getId());
      errors.put("turn", newTurn);
      errors.put("movingPieceId", trainingMoveCreationDTO.getMovingPieceId());
      throw new InvalidExceptionCustomize(errors);
    }

    if (((newTurn % 2 != 0) && !movingPieceDTO.isRed()) || ((newTurn % 2 == 0)
        && movingPieceDTO.isRed())) {
      log.warn("Training move attempted on opponent's turn for piece ID {}",
          trainingMoveCreationDTO.getMovingPieceId());
      Map<String, Object> errors = new HashMap<>();
      errors.put("message", Translator.toLocale("OPPONENT_TURN"));
      errors.put("trainingId", training.getId());
      errors.put("turn", newTurn);
      errors.put("movingPieceDTO", movingPieceDTO);
      throw new InvalidExceptionCustomize(errors);
    }

    boolean isAvailableMove = this.isAvailableMove(playBoardDTO, movingPieceDTO,
        trainingMoveCreationDTO.getToCol(), trainingMoveCreationDTO.getToRow());

    if (isAvailableMove) {
      MoveHistory moveHistory = moveHistoryMapper.toEntity(trainingMoveCreationDTO);
      moveHistory.setTurn(newTurn);
      moveHistoryRepository.save(moveHistory);

      log.info("Training move created successfully for piece ID {} to position ({}, {})",
          trainingMoveCreationDTO.getMovingPieceId(),
          trainingMoveCreationDTO.getToCol(), trainingMoveCreationDTO.getToRow());

      return this.makeMove(playBoardDTO, movingPieceDTO, trainingMoveCreationDTO.getToCol(),
          trainingMoveCreationDTO.getToRow());
    } else {
      log.warn("Invalid training move attempt for piece ID {} to position ({}, {})",
          trainingMoveCreationDTO.getMovingPieceId(),
          trainingMoveCreationDTO.getToCol(), trainingMoveCreationDTO.getToRow());
      Map<String, Object> errors = new HashMap<>();
      errors.put("message", Translator.toLocale("INVALID_MOVE"));
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
    Match match = matchRepository.findById(matchMoveCreationDTO.getMatchId()).orElseThrow(
        () -> new ResourceNotFoundExceptionCustomize(
            Collections.singletonMap("matchId", matchMoveCreationDTO.getMatchId())));

    if (match.getResult() != null) {
      log.error("Match with ID {} has already ended", match.getId());
      Map<String, Object> errors = new HashMap<>();
      errors.put("message", Translator.toLocale("END_MATCH"));
      errors.put("matchId", match.getId());
      throw new InvalidExceptionCustomize(errors);
    }

    if ((match.getPlayer1().getId() != matchMoveCreationDTO.getPlayerId()) && (
        match.getPlayer2().getId() != matchMoveCreationDTO.getPlayerId())) {
      log.error("Player ID {} is not part of the match ID {}", matchMoveCreationDTO.getPlayerId(),
          matchMoveCreationDTO.getMatchId());
      throw new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("playerId", matchMoveCreationDTO.getPlayerId()));
    }

    long newTurn = moveHistoryRepository.countTurnByMatch_Id(match.getId()) + 1;

    log.info("Creating match move for match ID {} on turn {}", match.getId(), newTurn);

    List<MoveHistory> moveHistories = moveHistoryRepository.findAllByMatch_Id(match.getId());
    PlayBoardDTO playBoardDTO = playBoardService.build(moveHistories);

    PieceDTO movingPieceDTO = pieceService.findOneInBoard(playBoardDTO,
        matchMoveCreationDTO.getMovingPieceId());

    if (movingPieceDTO == null) {
      log.error("Match move creation attempted with dead piece ID {}",
          matchMoveCreationDTO.getMovingPieceId());
      Map<String, Object> errors = new HashMap<>();
      errors.put("message", Translator.toLocale("DEAD_PIECE"));
      errors.put("matchId", match.getId());
      errors.put("turn", newTurn);
      errors.put("movingPieceId", matchMoveCreationDTO.getMovingPieceId());
      throw new InvalidExceptionCustomize(errors);
    }

    if (((newTurn % 2 != 0) && !movingPieceDTO.isRed()) || ((newTurn % 2 == 0)
        && movingPieceDTO.isRed())) {
      log.warn("Match move attempted on opponent's turn for piece ID {}",
          matchMoveCreationDTO.getMovingPieceId());
      Map<String, Object> errors = new HashMap<>();
      errors.put("message", Translator.toLocale("OPPONENT_TURN"));
      errors.put("matchId", match.getId());
      errors.put("turn", newTurn);
      errors.put("movingPieceDTO", movingPieceDTO);
      throw new InvalidExceptionCustomize(errors);
    }

    boolean isAvailableMove = this.isAvailableMove(playBoardDTO, movingPieceDTO,
        matchMoveCreationDTO.getToCol(), matchMoveCreationDTO.getToRow());

    if (isAvailableMove) {
      MoveHistory moveHistory = moveHistoryMapper.toEntity(matchMoveCreationDTO);
      moveHistory.setTurn(newTurn);
      moveHistoryRepository.save(moveHistory);

      log.info("Match move created successfully for piece ID {} to position ({}, {})",
          matchMoveCreationDTO.getMovingPieceId(),
          matchMoveCreationDTO.getToCol(), matchMoveCreationDTO.getToRow());

      return this.makeMove(playBoardDTO, movingPieceDTO, matchMoveCreationDTO.getToCol(),
          matchMoveCreationDTO.getToRow());
    } else {
      log.warn("Invalid match move attempt for piece ID {} to position ({}, {})",
          matchMoveCreationDTO.getMovingPieceId(),
          matchMoveCreationDTO.getToCol(), matchMoveCreationDTO.getToRow());
      Map<String, Object> errors = new HashMap<>();
      errors.put("message", Translator.toLocale("INVALID_MOVE"));
      errors.put("matchId", match.getId());
      errors.put("turn", newTurn);
      errors.put("movingPieceDTO", movingPieceDTO);
      errors.put("toCol", matchMoveCreationDTO.getToCol());
      errors.put("toRow", matchMoveCreationDTO.getToRow());
      throw new InvalidExceptionCustomize(errors);
    }
  }

  @Override
  public Map<Boolean, BestAvailableMoveResponseDTO> findAllBestAvailable(
      BestAvailableMoveRequestDTO bestAvailableMoveRequestDTO) {
    log.info("Starting findAllBestAvailable with request: {}", bestAvailableMoveRequestDTO);

    PlayBoardDTO playBoardDTO = bestAvailableMoveRequestDTO.getPlayBoardDTO();
    Integer depth = bestAvailableMoveRequestDTO.getDepth();

    // Initialize the response map
    Map<Boolean, BestAvailableMoveResponseDTO> bestAvailableMovesMap = new HashMap<>();

    // Retrieve all pieces from the board
    List<PieceDTO> redPieces = pieceService.findAllInBoard(playBoardDTO, null, true);
    List<PieceDTO> blackPieces = pieceService.findAllInBoard(playBoardDTO, null, false);

    log.debug("Red pieces: {}", redPieces);
    log.debug("Black pieces: {}", blackPieces);

    // Determine which pieces to evaluate based on the isRed parameter
    List<PieceDTO> piecesToEvaluate;
    if (bestAvailableMoveRequestDTO.getIsRed() == null) {
      piecesToEvaluate = Stream.concat(redPieces.stream(), blackPieces.stream())
          .collect(Collectors.toList());
    } else {
      piecesToEvaluate = bestAvailableMoveRequestDTO.getIsRed() ? redPieces : blackPieces;
    }

    log.debug("Pieces to evaluate: {}", piecesToEvaluate);

    // Find the best moves for each piece
    List<BestMoveResponseDTO> bestMoveResponseDTOs = piecesToEvaluate.stream()
        .map(pieceDTO -> findBestMoveForAPiece(playBoardDTO, pieceDTO, depth)).toList();

    // Aggregate results by color
    BestAvailableMoveResponseDTO redBestAvailableMoveResponse = new BestAvailableMoveResponseDTO();
    BestAvailableMoveResponseDTO blackBestAvailableMoveResponse = new BestAvailableMoveResponseDTO();

    // Populate the responses based on best move results
    if (bestAvailableMoveRequestDTO.getIsRed() == null || bestAvailableMoveRequestDTO.getIsRed()) {
      redBestAvailableMoveResponse.setBestMovesResponseDTOs(
          bestMoveResponseDTOs.stream().filter(dto -> dto.getPieceDTO().isRed())
              .collect(Collectors.toList()));
      redBestAvailableMoveResponse.setEvalScore(playBoardService.evaluate(playBoardDTO));
      bestAvailableMovesMap.put(true, redBestAvailableMoveResponse);
      log.info("Red best moves: {}",
          redBestAvailableMoveResponse.getBestMovesResponseDTOs().size());
    }

    if (bestAvailableMoveRequestDTO.getIsRed() == null || !bestAvailableMoveRequestDTO.getIsRed()) {
      blackBestAvailableMoveResponse.setBestMovesResponseDTOs(
          bestMoveResponseDTOs.stream().filter(dto -> !dto.getPieceDTO().isRed())
              .collect(Collectors.toList()));
      blackBestAvailableMoveResponse.setEvalScore(playBoardService.evaluate(playBoardDTO));
      bestAvailableMovesMap.put(false, blackBestAvailableMoveResponse);
      log.info("Black best moves: {}",
          blackBestAvailableMoveResponse.getBestMovesResponseDTOs().size());
    }

    return bestAvailableMovesMap;
  }

  private List<int[]> findAllAvailableMoveIndexes(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO) {
    log.debug("Finding all available move indexes for piece: {}", pieceDTO);

    int fromCol = 0;
    int fromRow = 0;
    int toCol = playBoardDTO.getState().length - 1;
    int toRow = playBoardDTO.getState()[0].length - 1;

    List<int[]> availableMoveIndexes = IntStream.rangeClosed(fromCol, toCol).boxed().flatMap(
        col -> IntStream.rangeClosed(fromRow, toRow)
            .filter(row -> this.isAvailableMove(playBoardDTO, pieceDTO, col, row))
            .mapToObj(row -> new int[]{col, row})).collect(Collectors.toList());

    log.debug("Available move indexes: {}", availableMoveIndexes);
    return availableMoveIndexes;
  }

  private Map<Long, MoveHistoryDTO> build(List<MoveHistory> moveHistories) {
    log.info("Building move history from moveHistories: {}", moveHistories);

    AtomicReference<PlayBoardDTO> playBoardDTO = new AtomicReference<>(playBoardService.generate());
    playBoardService.printTest("start: ", playBoardDTO.get(), null);
    AtomicReference<List<PieceDTO>> lastDeadPieceDTOs = new AtomicReference<>(new ArrayList<>());

    Map<Long, MoveHistoryDTO> moveHistoryDTOMap = new HashMap<>();

    // Create initial MoveHistoryDTO for turn 0
    MoveHistoryDTO initialMoveHistoryDTO = new MoveHistoryDTO();
    initialMoveHistoryDTO.setTurn(0L);
    initialMoveHistoryDTO.setPlayBoardDTO(playBoardDTO.get());
    moveHistoryDTOMap.put(0L, initialMoveHistoryDTO);

    // Process the rest of the move histories
    moveHistories.forEach(mh -> {
      MoveHistoryDTO moveHistoryDTO = new MoveHistoryDTO();
      moveHistoryDTO.setLastDeadPieceDTOs(lastDeadPieceDTOs.get());

      long turn = mh.getTurn();
      moveHistoryDTO.setTurn(turn);

      PieceDTO movingPieceDTO = pieceService.findOneInBoard(playBoardDTO.get(),
          mh.getPiece().getId());
      moveHistoryDTO.setMovingPieceDTO(movingPieceDTO);

      moveHistoryDTO.setToCol(mh.getToCol());
      moveHistoryDTO.setToRow(mh.getToRow());

      String description = moveDescriptionService.build(playBoardDTO.get(), movingPieceDTO,
          mh.getToCol(), mh.getToRow());
      moveHistoryDTO.setDescription(description);

      log.info("Turn {}: {}", turn, description);

      MoveDTO moveDTO = this.makeMove(playBoardDTO.get(), movingPieceDTO, mh.getToCol(),
          mh.getToRow());
      moveHistoryDTO.setDeadPieceDTO(moveDTO.getDeadPieceDTO());
      moveHistoryDTO.setPlayBoardDTO(moveDTO.getPlayBoardDTO());
      moveHistoryDTO.setCheckedGeneralPieceDTO(moveDTO.getCheckedGeneralPieceDTO());
      moveHistoryDTO.setCheckmateState(moveDTO.isCheckmateState());

      // Update board after moved for reusing in next turn
      playBoardDTO.set(moveDTO.getPlayBoardDTO());

      // Add deadPiece in this turn to list for reusing in next turn
      List<PieceDTO> tempLastDeadPieceDTOs = new ArrayList<>(lastDeadPieceDTOs.get());
      if (moveDTO.getDeadPieceDTO() != null) {
        tempLastDeadPieceDTOs.add(moveDTO.getDeadPieceDTO());
      }
      lastDeadPieceDTOs.set(tempLastDeadPieceDTOs);

      moveHistoryDTOMap.put(turn, moveHistoryDTO);
    });

    log.info("Built move history map with {} entries", moveHistoryDTOMap.size());
    return moveHistoryDTOMap;
  }

  private MoveDTO makeMove(PlayBoardDTO playBoardDTO, PieceDTO movingPieceDTO, int toCol,
      int toRow) {
    log.debug("Making move for piece {} to position ({}, {})", movingPieceDTO, toCol, toRow);

    MoveDTO moveDTO = new MoveDTO();
    moveDTO.setMovingPieceDTO(movingPieceDTO);
    moveDTO.setToCol(toCol);
    moveDTO.setToRow(toRow);

    PieceDTO deadPieceDTO = playBoardDTO.getState()[toCol][toRow];
    moveDTO.setDeadPieceDTO(deadPieceDTO);

    PlayBoardDTO updatedPlayBoardDTO = playBoardService.update(playBoardDTO, movingPieceDTO, toCol,
        toRow);
    moveDTO.setPlayBoardDTO(updatedPlayBoardDTO);

    moveDTO.setCheckedGeneralPieceDTO(
        this.findGeneralBeingChecked(moveDTO.getPlayBoardDTO(), movingPieceDTO.isRed()));

    moveDTO.setCheckmateState(this.isCheckmateState(updatedPlayBoardDTO, movingPieceDTO.isRed()));

    playBoardService.printTest("", moveDTO.getPlayBoardDTO(), movingPieceDTO);

    log.debug("Move result: {}", moveDTO);
    return moveDTO;
  }

  private PieceDTO findGeneralBeingChecked(PlayBoardDTO playBoardDTO, boolean isRed) {
    log.debug("Finding if general is being checked for color: {}", isRed);

    PieceDTO opponentGeneralPieceDTO = pieceService.findGeneralInBoard(playBoardDTO, !isRed);
    PieceDTO result = playBoardService.isGeneralBeingChecked(playBoardDTO, opponentGeneralPieceDTO)
        ? opponentGeneralPieceDTO : null;

    log.debug("General being checked: {}", result);
    return result;
  }

  // Checkmate state (End game) is the state that no any available moves found
  private boolean isCheckmateState(PlayBoardDTO playBoardDTO, boolean isRed) {
    log.debug("Checking for checkmate state. Red is: {}", isRed);

    int fromCol = 0;
    int fromRow = 0;
    int toCol = playBoardDTO.getState().length - 1;
    int toRow = playBoardDTO.getState()[0].length - 1;

    List<PieceDTO> opponentPiecesInBoard = pieceService.findAllInBoard(playBoardDTO, null, !isRed);
    log.debug("Opponent pieces on board: {}", opponentPiecesInBoard);

    boolean isCheckmate = opponentPiecesInBoard.stream().flatMap(
            opponentPiece -> IntStream.rangeClosed(fromCol, toCol).boxed().flatMap(
                col -> IntStream.rangeClosed(fromRow, toRow).mapToObj(row -> new int[]{col, row})
                    .filter(
                        index -> this.isAvailableMove(playBoardDTO, opponentPiece, index[0], index[1]))
                    .findFirst().stream()))
        .findAny()
        .isEmpty();

    log.info("Checkmate state result: {}", isCheckmate);
    return isCheckmate;
  }

  private boolean isAvailableMove(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, int toCol,
      int toRow) {
    log.debug("Checking available move for piece {} at destination ({}, {}).", pieceDTO, toCol,
        toRow);

    // Check if the move is valid
    boolean isValidMove = moveRuleService.isValid(playBoardDTO, pieceDTO, toCol, toRow);
    log.debug("Move validity for piece {} to ({}, {}): {}", pieceDTO, toCol, toRow, isValidMove);

    if (isValidMove) {
      // Update the playboard with the move
      PlayBoardDTO updatedPlayBoardDTO = playBoardService.update(playBoardDTO, pieceDTO, toCol,
          toRow);
      log.debug("Playboard updated after move. Checking general safety...");

      // Find the general piece on the updated board
      PieceDTO generalPieceDTO = pieceService.findGeneralInBoard(updatedPlayBoardDTO,
          pieceDTO.isRed());

      // Check if the general is safe after the move
      boolean isGeneralSafe = playBoardService.isGeneralInSafe(updatedPlayBoardDTO,
          generalPieceDTO);
      log.debug("General safety status after move: {}", isGeneralSafe);

      return isGeneralSafe;
    } else {
      log.debug("Move is not valid. Returning false.");
      return false;
    }
  }

  private BestMoveResponseDTO findBestMoveForAPiece(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO,
      int depth) {
    List<int[]> availableMoves = findAllAvailableMoveIndexes(playBoardDTO, pieceDTO);

    // Sort moves based on a heuristic (e.g., capturing opponent's pieces first)
    availableMoves.sort((move1, move2) -> Integer.compare(playBoardService.evaluate(
            playBoardService.update(playBoardDTO, pieceDTO, move2[0], move2[1])),
        playBoardService.evaluate(
            playBoardService.update(playBoardDTO, pieceDTO, move1[0], move1[1]))));

    BestMoveResponseDTO bestMoveResponseDTO = new BestMoveResponseDTO();
    bestMoveResponseDTO.setPieceDTO(pieceDTO);

    int bestScore = Integer.MIN_VALUE;
    List<int[]> bestMoves = new ArrayList<>();

    for (int[] move : availableMoves) {
      PlayBoardDTO newPlayBoardDTO = playBoardService.update(playBoardDTO, pieceDTO, move[0],
          move[1]);
      int score = minimax(newPlayBoardDTO, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

      if (score > bestScore) {
        bestScore = score;
        bestMoves.clear();
        bestMoves.add(move);
      } else if (score == bestScore) {
        bestMoves.add(move);
      }
    }

    bestMoveResponseDTO.setBestAvailableMoveIndexes(bestMoves);
    return bestMoveResponseDTO;
  }

  private int minimax(PlayBoardDTO playBoardDTO, int depth, int alpha, int beta,
      boolean isMaximizing) {

    // Check for transposition table
    if (transpositionTable.containsKey(playBoardDTO)) {
      return transpositionTable.get(playBoardDTO);
    }

    boolean isEndGame = isCheckmateState(playBoardDTO, !isMaximizing);
    // Terminal condition
    if (depth == 0 || isEndGame) {
      int eval = playBoardService.evaluate(playBoardDTO);
      transpositionTable.put(playBoardDTO, eval);
      return eval;
    }

    int result;
    if (isMaximizing) {
      int maxEval = Integer.MIN_VALUE;
      List<PieceDTO> alivePieceDTO = pieceService.findAllInBoard(playBoardDTO, null, true);
      for (PieceDTO pieceDTO : alivePieceDTO) {
        List<int[]> availableMoves = findAllAvailableMoveIndexes(playBoardDTO, pieceDTO);
        for (int[] move : availableMoves) {
          PlayBoardDTO newPlayBoardDTO = playBoardService.update(playBoardDTO, pieceDTO, move[0],
              move[1]);
          int eval = minimax(newPlayBoardDTO, depth - 1, alpha, beta, false);
          maxEval = Math.max(maxEval, eval);
          alpha = Math.max(alpha, eval);
          if (beta <= alpha) {
            break; // Beta cut-off
          }
        }
      }
      result = maxEval;
    } else {
      int minEval = Integer.MAX_VALUE;
      List<PieceDTO> piecesToEvaluate = pieceService.findAllInBoard(playBoardDTO, null, false);
      for (PieceDTO pieceDTO : piecesToEvaluate) {
        List<int[]> availableMoves = findAllAvailableMoveIndexes(playBoardDTO, pieceDTO);
        for (int[] move : availableMoves) {
          PlayBoardDTO newPlayBoardDTO = playBoardService.update(playBoardDTO, pieceDTO, move[0],
              move[1]);
          int eval = minimax(newPlayBoardDTO, depth - 1, alpha, beta, true);
          minEval = Math.min(minEval, eval);
          beta = Math.min(beta, eval);
          if (beta <= alpha) {
            break; // Alpha cut-off
          }
        }
      }
      result = minEval;
    }

    // Store the result in the transposition table
    transpositionTable.put(playBoardDTO, result);
    return result;
  }

}
