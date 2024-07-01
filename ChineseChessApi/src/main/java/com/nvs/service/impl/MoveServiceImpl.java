package com.nvs.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.nvs.common.ErrorMessage;
import com.nvs.config.exception.InvalidExceptionCustomize;
import com.nvs.config.exception.ResourceNotFoundExceptionCustomize;
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

import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor public class MoveServiceImpl implements MoveService{

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
   public Map<Long, MoveHistoryDTO> findAllByMatchId(long matchId){
      if(!matchRepository.existsById(matchId)){
         throw new ResourceNotFoundExceptionCustomize(Collections.singletonMap("matchId", matchId));
      }
      return this.build(moveHistoryRepository.findAllByMatch_Id(matchId));
   }

   @Override
   public Map<Long, MoveHistoryDTO> findAllByTrainingId(long trainingId){
      if(!trainingRepository.existsById(trainingId)){
         throw new ResourceNotFoundExceptionCustomize(Collections.singletonMap("trainingId", trainingId));
      }
      return this.build(moveHistoryRepository.findAllByTraining_Id(trainingId));
   }

   @Override
   public List<int[]> findAllAvailable(AvailableMoveRequestDTO availableMoveRequestDTO){
      PieceDTO movingPieceDTO = pieceService.findOneInBoard(availableMoveRequestDTO.getPlayBoardDTO(), availableMoveRequestDTO.getMovingPieceId());

      if(movingPieceDTO == null){
         Map<String, Object> errors = new HashMap<>();
         errors.put("message", ErrorMessage.DEAD_PIECE);
         errors.put("movingPieceId", availableMoveRequestDTO.getMovingPieceId());

         throw new InvalidExceptionCustomize(errors);
      }

      List<int[]> availableMoveIndexes = this.findAllAvailableMoveIndexes(availableMoveRequestDTO.getPlayBoardDTO(), movingPieceDTO);

      playBoardService.printTest(availableMoveRequestDTO.getPlayBoardDTO(), movingPieceDTO, availableMoveIndexes);

      return availableMoveIndexes;
   }

   @Override
   public MoveDTO create(MoveCreationDTO moveCreationDTO){
      PieceDTO movingPieceDTO = pieceService.findOneInBoard(moveCreationDTO.getPlayBoardDTO(), moveCreationDTO.getMovingPieceId());

      if(movingPieceDTO == null){
         throw new InvalidExceptionCustomize(ErrorMessage.DEAD_PIECE, Collections.singletonMap("movingPieceId", moveCreationDTO.getMovingPieceId()));
      }

      boolean isAvailableMove = this.isAvailableMove(moveCreationDTO.getPlayBoardDTO(), movingPieceDTO, moveCreationDTO.getToCol(), moveCreationDTO.getToRow());

      if(isAvailableMove){
         return this.makeMove(moveCreationDTO.getPlayBoardDTO(), movingPieceDTO, moveCreationDTO.getToCol(), moveCreationDTO.getToRow());
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
   public MoveDTO create(TrainingMoveCreationDTO trainingMoveCreationDTO){
      Training training = trainingRepository.findById(trainingMoveCreationDTO.getTrainingId())
                                            .orElseThrow(()->new ResourceNotFoundExceptionCustomize(Collections.singletonMap("trainingId", trainingMoveCreationDTO.getTrainingId())));

      long newTurn = moveHistoryRepository.countTurnByTraining_Id(training.getId()) + 1;

      List<MoveHistory> moveHistories = moveHistoryRepository.findAllByTraining_Id(training.getId());
      PlayBoardDTO playBoardDTO = playBoardService.build(moveHistories);

      // check piece input existing in playBoardDTO
      PieceDTO movingPieceDTO = pieceService.findOneInBoard(playBoardDTO, trainingMoveCreationDTO.getMovingPieceId());
      if(movingPieceDTO == null){
         Map<String, Object> errors = new HashMap<>();
         errors.put("message", ErrorMessage.DEAD_PIECE);
         errors.put("trainingId", training.getId());
         errors.put("turn", newTurn);
         errors.put("movingPieceId", trainingMoveCreationDTO.getMovingPieceId());

         throw new InvalidExceptionCustomize(errors);
      }

      if(((newTurn % 2 != 0) && !movingPieceDTO.isRed()) || ((newTurn % 2 == 0) && movingPieceDTO.isRed())){
         Map<String, Object> errors = new HashMap<>();
         errors.put("message", ErrorMessage.OPPONENT_TURN);
         errors.put("trainingId", training.getId());
         errors.put("turn", newTurn);
         errors.put("movingPieceDTO", movingPieceDTO);

         throw new InvalidExceptionCustomize(errors);
      }

      boolean isAvailableMove = this.isAvailableMove(playBoardDTO, movingPieceDTO, trainingMoveCreationDTO.getToCol(), trainingMoveCreationDTO.getToRow());

      if(isAvailableMove){
         MoveHistory moveHistory = moveHistoryMapper.toEntity(trainingMoveCreationDTO);
         moveHistory.setTurn(newTurn);
         moveHistoryRepository.save(moveHistory);

         return this.makeMove(playBoardDTO, movingPieceDTO, trainingMoveCreationDTO.getToCol(), trainingMoveCreationDTO.getToRow());
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
   public MoveDTO create(MatchMoveCreationDTO matchMoveCreationDTO){
      Match match = matchRepository.findById(matchMoveCreationDTO.getMatchId())
                                   .orElseThrow(()->new ResourceNotFoundExceptionCustomize(Collections.singletonMap("matchId", matchMoveCreationDTO.getMatchId())));

      if(match.getResult() != null){
         Map<String, Object> errors = new HashMap<>();
         errors.put("message", ErrorMessage.END_MATCH);
         errors.put("matchId", match.getId());

         throw new InvalidExceptionCustomize(errors);
      }

      if((match.getPlayer1().getId() != matchMoveCreationDTO.getPlayerId()) && (match.getPlayer2().getId() != matchMoveCreationDTO.getPlayerId())){
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
      if(movingPieceDTO == null){
         Map<String, Object> errors = new HashMap<>();
         errors.put("message", ErrorMessage.DEAD_PIECE);
         errors.put("matchId", match.getId());
         errors.put("turn", newTurn);
         errors.put("movingPieceId", matchMoveCreationDTO.getMovingPieceId());

         throw new InvalidExceptionCustomize(errors);
      }

      // check piece turn
      if(((newTurn % 2 != 0) && !movingPieceDTO.isRed()) || ((newTurn % 2 == 0) && movingPieceDTO.isRed())){
         Map<String, Object> errors = new HashMap<>();
         errors.put("message", ErrorMessage.OPPONENT_TURN);
         errors.put("matchId", match.getId());
         errors.put("turn", newTurn);
         errors.put("movingPieceDTO", movingPieceDTO);

         throw new InvalidExceptionCustomize(errors);
      }

      // check player move valid piece
      if(movingPieceDTO.isRed() && Objects.equals(match.getPlayer1().getId(), matchMoveCreationDTO.getPlayerId())){
         Map<String, Object> errors = new HashMap<>();
         errors.put("message", ErrorMessage.INVALID_PLAYER_MOVE_PIECE);
         errors.put("matchId", match.getId());
         errors.put("playerId", matchMoveCreationDTO.getMovingPieceId());
         errors.put("turn", newTurn);
         errors.put("movingPieceDTO", movingPieceDTO);

         throw new InvalidExceptionCustomize(errors);
      }

      boolean isAvailableMove = this.isAvailableMove(playBoardDTO, movingPieceDTO, matchMoveCreationDTO.getToCol(), matchMoveCreationDTO.getToRow());

      if(isAvailableMove){
         MoveHistory moveHistory = moveHistoryMapper.toEntity(matchMoveCreationDTO);
         moveHistory.setTurn(newTurn);

         moveHistoryRepository.save(moveHistory);

         return this.makeMove(playBoardDTO, movingPieceDTO, matchMoveCreationDTO.getToCol(), matchMoveCreationDTO.getToRow());
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
   public Map<Boolean, BestAvailableMoveResponseDTO> findAllBestAvailable(BestAvailableMoveRequestDTO bestAvailableMoveRequestDTO){
      PlayBoardDTO playBoardDTO = bestAvailableMoveRequestDTO.getPlayBoardDTO();
      Integer depth = bestAvailableMoveRequestDTO.getDepth();

      // Initialize the response map
      Map<Boolean, BestAvailableMoveResponseDTO> bestAvailableMovesMap = new HashMap<>();

      // Retrieve all pieces from the board
      List<PieceDTO> redPieces = pieceService.findAllInBoard(playBoardDTO, null, true);
      List<PieceDTO> blackPieces = pieceService.findAllInBoard(playBoardDTO, null, false);

      // Determine which pieces to evaluate based on the isRed parameter
      List<PieceDTO> piecesToEvaluate;
      if(bestAvailableMoveRequestDTO.getIsRed() == null){
         // If isRed is null, evaluate all pieces
         piecesToEvaluate = Stream.concat(redPieces.stream(), blackPieces.stream()).collect(Collectors.toList());
      } else {
         // Evaluate only the pieces of the specified color
         piecesToEvaluate = bestAvailableMoveRequestDTO.getIsRed() ? redPieces : blackPieces;
      }

      // Find the best moves for each piece
      List<BestMoveResponseDTO> bestMoveResponseDTOs = piecesToEvaluate.stream()
                                                                       .map(pieceDTO->findBestMoveForAPiece(playBoardDTO, pieceDTO, depth))
                                                                       .toList();

      // Aggregate results by color
      BestAvailableMoveResponseDTO redBestAvailableMoveResponse = new BestAvailableMoveResponseDTO();
      BestAvailableMoveResponseDTO blackBestAvailableMoveResponse = new BestAvailableMoveResponseDTO();

      // Populate the responses based on best move results
      if(bestAvailableMoveRequestDTO.getIsRed() == null || bestAvailableMoveRequestDTO.getIsRed()){
         redBestAvailableMoveResponse.setBestMovesResponseDTOs(bestMoveResponseDTOs.stream()
                                                                                   .filter(dto->dto.getPieceDTO().isRed())
                                                                                   .collect(Collectors.toList()));
         redBestAvailableMoveResponse.setEvalScore(playBoardService.evaluate(playBoardDTO));
         bestAvailableMovesMap.put(true, redBestAvailableMoveResponse);
         System.out.println("red: " + redBestAvailableMoveResponse.getBestMovesResponseDTOs().size());
      }

      if(bestAvailableMoveRequestDTO.getIsRed() == null || !bestAvailableMoveRequestDTO.getIsRed()){
         blackBestAvailableMoveResponse.setBestMovesResponseDTOs(bestMoveResponseDTOs.stream()
                                                                                     .filter(dto->!dto.getPieceDTO().isRed())
                                                                                     .collect(Collectors.toList()));
         blackBestAvailableMoveResponse.setEvalScore(playBoardService.evaluate(playBoardDTO));
         bestAvailableMovesMap.put(false, blackBestAvailableMoveResponse);
         System.out.println("black: " + blackBestAvailableMoveResponse.getBestMovesResponseDTOs().size());

      }

      return bestAvailableMovesMap;
   }

   private List<int[]> findAllAvailableMoveIndexes(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO){
      int fromCol = 0;
      int fromRow = 0;
      int toCol = playBoardDTO.getState().length - 1;
      int toRow = playBoardDTO.getState()[0].length - 1;

      return IntStream.rangeClosed(fromCol, toCol)
                      .boxed()
                      .flatMap(col->IntStream.rangeClosed(fromRow, toRow)
                                             .filter(row->this.isAvailableMove(playBoardDTO, pieceDTO, col, row))
                                             .mapToObj(row->new int[]{col, row}))
                      .collect(Collectors.toList());
   }

   private Map<Long, MoveHistoryDTO> build(List<MoveHistory> moveHistories){
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
      moveHistories.forEach(mh->{
         MoveHistoryDTO moveHistoryDTO = new MoveHistoryDTO();
         moveHistoryDTO.setLastDeadPieceDTOs(lastDeadPieceDTOs.get());

         long turn = mh.getTurn();
         moveHistoryDTO.setTurn(turn);

         PieceDTO movingPieceDTO = pieceService.findOneInBoard(playBoardDTO.get(), mh.getPiece().getId());
         moveHistoryDTO.setMovingPieceDTO(movingPieceDTO);

         moveHistoryDTO.setToCol(mh.getToCol());
         moveHistoryDTO.setToRow(mh.getToRow());

         String description = moveDescriptionService.build(playBoardDTO.get(), movingPieceDTO, mh.getToCol(), mh.getToRow());
         moveHistoryDTO.setDescription(description);

         System.out.println("\nTurn " + turn + ": \t\t" + description);

         MoveDTO moveDTO = this.makeMove(playBoardDTO.get(), movingPieceDTO, mh.getToCol(), mh.getToRow());
         moveHistoryDTO.setDeadPieceDTO(moveDTO.getDeadPieceDTO());
         moveHistoryDTO.setPlayBoardDTO(moveDTO.getPlayBoardDTO());
         moveHistoryDTO.setCheckedGeneralPieceDTO(moveDTO.getCheckedGeneralPieceDTO());
         moveHistoryDTO.setCheckmateState(moveDTO.isCheckmateState());

         // Update board after moved for reusing in next turn
         playBoardDTO.set(moveDTO.getPlayBoardDTO());

         // Add deadPiece in this turn to list for reusing in next turn
         List<PieceDTO> tempLastDeadPieceDTOs = new ArrayList<>(lastDeadPieceDTOs.get());
         if(moveDTO.getDeadPieceDTO() != null){
            tempLastDeadPieceDTOs.add(moveDTO.getDeadPieceDTO());
         }
         lastDeadPieceDTOs.set(tempLastDeadPieceDTOs);

         moveHistoryDTOMap.put(turn, moveHistoryDTO);
      });

      return moveHistoryDTOMap;
   }

   private MoveDTO makeMove(PlayBoardDTO playBoardDTO, PieceDTO movingPieceDTO, int toCol, int toRow){
      MoveDTO moveDTO = new MoveDTO();
      moveDTO.setMovingPieceDTO(movingPieceDTO);
      moveDTO.setToCol(toCol);
      moveDTO.setToRow(toRow);

      PieceDTO deadPieceDTO = playBoardDTO.getState()[toCol][toRow];
      moveDTO.setDeadPieceDTO(deadPieceDTO);

      PlayBoardDTO updatedPlayBoardDTO = playBoardService.update(playBoardDTO, movingPieceDTO, toCol, toRow);
      moveDTO.setPlayBoardDTO(updatedPlayBoardDTO);

      moveDTO.setCheckedGeneralPieceDTO(this.findGeneralBeingChecked(moveDTO.getPlayBoardDTO(), movingPieceDTO.isRed()));

      moveDTO.setCheckmateState(this.isCheckmateState(updatedPlayBoardDTO, movingPieceDTO.isRed()));

      playBoardService.printTest("", moveDTO.getPlayBoardDTO(), movingPieceDTO);

      return moveDTO;
   }

   private PieceDTO findGeneralBeingChecked(PlayBoardDTO playBoardDTO, boolean isRed){
      PieceDTO opponentGeneralPieceDTO = pieceService.findGeneralInBoard(playBoardDTO, !isRed);
      return playBoardService.isGeneralBeingChecked(playBoardDTO, opponentGeneralPieceDTO) ? opponentGeneralPieceDTO : null;
   }

   // Checkmate state (End game) is the state that no any available moves found
   private boolean isCheckmateState(PlayBoardDTO playBoardDTO, boolean isRed){
      int fromCol = 0;
      int fromRow = 0;
      int toCol = playBoardDTO.getState().length - 1;
      int toRow = playBoardDTO.getState()[0].length - 1;
      List<PieceDTO> opponentPiecesInBoard = pieceService.findAllInBoard(playBoardDTO, null, !isRed);

      return opponentPiecesInBoard.stream()
                                  .flatMap(opponentPiece->IntStream.rangeClosed(fromCol, toCol)
                                                                   .boxed()
                                                                   .flatMap(col->IntStream.rangeClosed(fromRow, toRow)
                                                                                          .mapToObj(row->new int[]{col, row})
                                                                                          .filter(index->this.isAvailableMove(playBoardDTO, opponentPiece, index[0], index[1]))
                                                                                          .findFirst()
                                                                                          .stream()))
                                  .findAny()
                                  .isEmpty();
   }

   private boolean isAvailableMove(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, int toCol, int toRow){
      // available move is valid move and the same color general is safe after move
      boolean isValidMove = moveRuleService.isValid(playBoardDTO, pieceDTO, toCol, toRow);
      if(isValidMove){
         PlayBoardDTO updatedPlayBoardDTO = playBoardService.update(playBoardDTO, pieceDTO, toCol, toRow);
         PieceDTO generalPieceDTO = pieceService.findGeneralInBoard(updatedPlayBoardDTO, pieceDTO.isRed());

         return playBoardService.isGeneralInSafe(updatedPlayBoardDTO, generalPieceDTO);
      } else {
         return false;
      }
   }

   private BestMoveResponseDTO findBestMoveForAPiece(PlayBoardDTO playBoardDTO, PieceDTO pieceDTO, int depth){
      List<int[]> availableMoves = findAllAvailableMoveIndexes(playBoardDTO, pieceDTO);

      // Sort moves based on a heuristic (e.g., capturing opponent's pieces first)
      availableMoves.sort((move1, move2)->Integer.compare(playBoardService.evaluate(playBoardService.update(playBoardDTO, pieceDTO, move2[0], move2[1])), playBoardService.evaluate(playBoardService.update(playBoardDTO, pieceDTO, move1[0], move1[1]))));

      BestMoveResponseDTO bestMoveResponseDTO = new BestMoveResponseDTO();
      bestMoveResponseDTO.setPieceDTO(pieceDTO);

      int bestScore = Integer.MIN_VALUE;
      List<int[]> bestMoves = new ArrayList<>();

      for(int[] move : availableMoves){
         PlayBoardDTO newPlayBoardDTO = playBoardService.update(playBoardDTO, pieceDTO, move[0], move[1]);
         int score = minimax(newPlayBoardDTO, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

         if(score > bestScore){
            bestScore = score;
            bestMoves.clear();
            bestMoves.add(move);
         } else if(score == bestScore){
            bestMoves.add(move);
         }
      }

      bestMoveResponseDTO.setBestAvailableMoveIndexes(bestMoves);
      return bestMoveResponseDTO;
   }

   private int minimax(PlayBoardDTO playBoardDTO, int depth, int alpha, int beta, boolean isMaximizing){

      // Check for transposition table
      if(transpositionTable.containsKey(playBoardDTO)){
         return transpositionTable.get(playBoardDTO);
      }

      boolean isEndGame = isCheckmateState(playBoardDTO, !isMaximizing);
      // Terminal condition
      if(depth == 0 || isEndGame){
         int eval = playBoardService.evaluate(playBoardDTO);
         transpositionTable.put(playBoardDTO, eval);
         return eval;
      }

      int result;
      if(isMaximizing){
         int maxEval = Integer.MIN_VALUE;
         List<PieceDTO> alivePieceDTO = pieceService.findAllInBoard(playBoardDTO, null, true);
         for(PieceDTO pieceDTO : alivePieceDTO){
            List<int[]> availableMoves = findAllAvailableMoveIndexes(playBoardDTO, pieceDTO);
            for(int[] move : availableMoves){
               PlayBoardDTO newPlayBoardDTO = playBoardService.update(playBoardDTO, pieceDTO, move[0], move[1]);
               int eval = minimax(newPlayBoardDTO, depth - 1, alpha, beta, false);
               maxEval = Math.max(maxEval, eval);
               alpha = Math.max(alpha, eval);
               if(beta <= alpha){
                  break; // Beta cut-off
               }
            }
         }
         result = maxEval;
      } else {
         int minEval = Integer.MAX_VALUE;
         List<PieceDTO> piecesToEvaluate = pieceService.findAllInBoard(playBoardDTO, null, false);
         for(PieceDTO pieceDTO : piecesToEvaluate){
            List<int[]> availableMoves = findAllAvailableMoveIndexes(playBoardDTO, pieceDTO);
            for(int[] move : availableMoves){
               PlayBoardDTO newPlayBoardDTO = playBoardService.update(playBoardDTO, pieceDTO, move[0], move[1]);
               int eval = minimax(newPlayBoardDTO, depth - 1, alpha, beta, true);
               minEval = Math.min(minEval, eval);
               beta = Math.min(beta, eval);
               if(beta <= alpha){
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
