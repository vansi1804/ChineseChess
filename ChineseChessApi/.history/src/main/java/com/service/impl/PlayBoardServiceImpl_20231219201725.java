package com.service.impl;

import com.common.Default;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.data.entity.MoveHistory;
import com.data.entity.Piece;
import com.data.mapper.PieceMapper;
import com.data.repository.PieceRepository;
import com.service.MoveRuleService;
import com.service.PieceService;
import com.service.PlayBoardService;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;

@Service
public class PlayBoardServiceImpl implements PlayBoardService {

  private final int COL_MIN = Default.Game.PlayBoardSize.COL_MIN;
  private final int ROW_MIN = Default.Game.PlayBoardSize.ROW_MIN;
  private final int COL_MAX = Default.Game.PlayBoardSize.COL_MAX;
  private final int ROW_MAX = Default.Game.PlayBoardSize.ROW_MAX;

  private final PieceRepository pieceRepository;
  private final PieceMapper pieceMapper;
  private final PieceService pieceService;
  private final MoveRuleService moveRuleService;

  public PlayBoardServiceImpl(
    PieceRepository pieceRepository,
    PieceMapper pieceMapper,
    PieceService pieceService,
    MoveRuleService moveRuleService
  ) {
    this.pieceRepository = pieceRepository;
    this.pieceMapper = pieceMapper;
    this.pieceService = pieceService;
    this.moveRuleService = moveRuleService;
  }

  @Override
  public PlayBoardDTO generate() {
    PlayBoardDTO playBoardDTO = new PlayBoardDTO(
      new PieceDTO[COL_MAX + 1][ROW_MAX + 1]
    );
    List<Piece> pieces = pieceRepository.findAll();

    pieces
      .stream()
      .map(p -> pieceMapper.toDTO(p))
      .forEach(pDTO ->
        playBoardDTO.getState()[pDTO.getCurrentCol()][pDTO.getCurrentRow()] =
          pDTO
      );

    printTest(null, playBoardDTO, null);

    return playBoardDTO;
  }

  @Override
  public PlayBoardDTO build(List<MoveHistory> moveHistories) {
    return moveHistories
      .stream()
      .reduce(
        generate(),
        (playBoardDTO, mh) ->
          update(
            playBoardDTO,
            pieceService.findOneInBoard(playBoardDTO, mh.getPiece().getId()),
            mh.getToCol(),
            mh.getToRow()
          ),
        (playBoardDTO, updatedBoardDTO) -> updatedBoardDTO
      );
  }

  @Override
  public PlayBoardDTO update(
    PlayBoardDTO playBoardDTO,
    PieceDTO pieceDTO,
    int toCol,
    int toRow
  ) {
    PlayBoardDTO updatePlayBoardDTO = new PlayBoardDTO(
      pieceMapper.copy(playBoardDTO.getState())
    );

    updatePlayBoardDTO.getState()[pieceDTO.getCurrentCol()][pieceDTO.getCurrentRow()] =
      null;

    PieceDTO updatedPieceDTO = pieceMapper.copy(pieceDTO);
    updatedPieceDTO.setCurrentCol(toCol);
    updatedPieceDTO.setCurrentRow(toRow);

    updatePlayBoardDTO.getState()[toCol][toRow] = updatedPieceDTO;

    return updatePlayBoardDTO;
  }

  @Override
  public boolean areTwoGeneralsFacing(
    PlayBoardDTO playBoardDTO,
    PieceDTO generalPieceDTO1,
    PieceDTO generalPieceDTO2
  ) {
    if (generalPieceDTO1.getCurrentCol() == generalPieceDTO2.getCurrentCol()) {
      int currentCol = generalPieceDTO1.getCurrentCol();
      int fromRow = generalPieceDTO1.getCurrentRow();
      int toRow = generalPieceDTO2.getCurrentRow();

      return !pieceService.existsBetweenInColPath(
        playBoardDTO,
        currentCol,
        fromRow,
        toRow
      );
    }

    return false;
  }

  @Override
  public boolean isGeneralBeingChecked(
    PlayBoardDTO playBoardDTO,
    PieceDTO generalPieceDTO
  ) {
    List<PieceDTO> opponentPieceDTOsInBoard = pieceService.findAllInBoard(
      playBoardDTO,
      null,
      !generalPieceDTO.isRed()
    );

    return opponentPieceDTOsInBoard
      .stream()
      .anyMatch(opponentPiece ->
        moveRuleService.isValid(
          playBoardDTO,
          opponentPiece,
          generalPieceDTO.getCurrentCol(),
          generalPieceDTO.getCurrentRow()
        )
      );
  }

  @Override
  public boolean isGeneralInSafe(
    PlayBoardDTO playBoardDTO,
    PieceDTO generalPieceDTO
  ) {
    PieceDTO opponentGeneralDTO = pieceService.findGeneralInBoard(
      playBoardDTO,
      !generalPieceDTO.isRed()
    );

    return (
      !areTwoGeneralsFacing(
        playBoardDTO,
        generalPieceDTO,
        opponentGeneralDTO
      ) &&
      !isGeneralBeingChecked(playBoardDTO, generalPieceDTO)
    );
  }

  @Override
  public int evaluate(PlayBoardDTO playBoardDTO) {
    return IntStream
      .rangeClosed(COL_MIN, COL_MAX)
      .flatMap(col ->
        IntStream
          .rangeClosed(ROW_MIN, ROW_MAX)
          .filter(row -> playBoardDTO.getState()[col][row] != null)
          .map(row -> {
            PieceDTO pieceDTO = playBoardDTO.getState()[col][row];
            int piecePower = pieceService
              .convertByName(pieceDTO.getName())
              .getPower();

            return pieceDTO.isRed() ? piecePower : -piecePower;
          })
      )
      .sum();
  }

  @Override
  public void printTest(
    Object title,
    PlayBoardDTO playBoardDTO,
    PieceDTO pieceDTO
  ) {
    System.out.println("\n===========================================");
    System.out.println(String.valueOf(title));
    System.out.println("===========================================");

    for (int row = ROW_MIN; row <= ROW_MAX; row++) {
      for (int col = COL_MIN; col <= COL_MAX; col++) {
        PieceDTO targetPieceDTO = playBoardDTO.getState()[col][row];
        System.out.print(
          this.getSymbolOutput(pieceDTO, col, row, targetPieceDTO, false)
        );
      }
      System.out.println("\n\n");
    }

    System.out.println("===========================================");
  }

  @Override
  public void printTest(
    PlayBoardDTO playBoardDTO,
    PieceDTO pieceDTO,
    List<int[]> availableMoveIndexes
  ) {
    System.out.println("\n===========================================");
    System.out.println("Available move: ");
    System.out.println("===========================================");

    for (int row = ROW_MIN; row <= ROW_MAX; row++) {
      for (int col = COL_MIN; col <= COL_MAX; col++) {
        PieceDTO targetPieceDTO = playBoardDTO.getState()[col][row];
        int[] index = new int[] { col, row };
        boolean containsIndex = availableMoveIndexes
          .stream()
          .anyMatch(arr -> Arrays.equals(arr, index));
        if (containsIndex) {
          System.out.print(
            this.getSymbolOutput(null, col, row, targetPieceDTO, true)
          );
        } else {
          System.out.print(
            this.getSymbolOutput(pieceDTO, col, row, targetPieceDTO, false)
          );
        }
      }
      System.out.println("\n\n\n");
    }

    System.out.println("===========================================");
  }

  private String getSymbolOutput(
    PieceDTO movingPieceDTO,
    int col,
    int row,
    PieceDTO targetPieceDTO,
    boolean isValidMoveFinding
  ) {
    if (targetPieceDTO == null) {
      if (
        (movingPieceDTO != null) &&
        (
          (col == movingPieceDTO.getCurrentCol()) &&
          (row == movingPieceDTO.getCurrentRow())
        )
      ) {
        return "   [ ]   ";
      } else if (isValidMoveFinding) {
        return "    O    ";
      } else {
        return "    +    ";
      }
    } else {
      String symbol =
        this.formatNameSymbol(
            pieceService.convertByName(targetPieceDTO.getName()).getShortName(),
            targetPieceDTO.isRed(),
            targetPieceDTO.getId()
          );

      if (
        (movingPieceDTO != null) &&
        (targetPieceDTO.getId() == movingPieceDTO.getId())
      ) {
        return "[" + symbol + "]";
      } else if (isValidMoveFinding) {
        return "(" + symbol + ")";
      } else {
        return " " + symbol + " ";
      }
    }
  }

  private String formatNameSymbol(String shortName, boolean isRed, int id) {
    return (
      (shortName.length() == 1 ? " " + shortName : shortName) +
      this.formatColorSymbol(isRed) +
      "_" +
      (
        shortName.length() == 1
          ? this.formatIdSymbol(id) + " "
          : this.formatIdSymbol(id)
      )
    );
  }

  private String formatColorSymbol(boolean isRed) {
    return isRed ? "1" : "0";
  }

  private String formatIdSymbol(int id) {
    String idSymbol = String.valueOf(id);
    return idSymbol.length() == 1 ? "0" + idSymbol : idSymbol; // custom symbolID: id = {1 -> 01; 10 -> 10}
  }
}
