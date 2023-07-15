package com.example.chinesechesstrainning.support;

import com.example.chinesechesstrainning.R;
import com.example.chinesechesstrainning.model.Move;
import com.example.chinesechesstrainning.model.PlayBoard;
import com.example.chinesechesstrainning.model.Training;
import com.example.chinesechesstrainning.model.TrainingDetail;
import com.example.chinesechesstrainning.model.Piece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class DataTest {

    public static List<Piece> pieceData() {
        List<Piece> pieces = new ArrayList<>();
        pieces.add(new Piece(1, "Tốt", true, 0, 6, R.drawable.red_soldier));
        pieces.add(new Piece(2, "Tốt", true, 2, 6, R.drawable.red_soldier));
        pieces.add(new Piece(3, "Tốt", true, 4, 6, R.drawable.red_soldier));
        pieces.add(new Piece(4, "Tốt", true, 6, 6, R.drawable.red_soldier));
        pieces.add(new Piece(5, "Tốt", true, 8, 6, R.drawable.red_soldier));
        pieces.add(new Piece(6, "Pháo", true, 1, 7, R.drawable.red_cannon));
        pieces.add(new Piece(7, "Pháo", true, 7, 7, R.drawable.red_cannon));
        pieces.add(new Piece(8, "Xe", true, 0, 9, R.drawable.red_chariot));
        pieces.add(new Piece(9, "Xe", true, 8, 9, R.drawable.red_chariot));
        pieces.add(new Piece(10, "Mã", true, 1, 9, R.drawable.red_horse));
        pieces.add(new Piece(11, "Mã", true, 7, 9, R.drawable.red_horse));
        pieces.add(new Piece(12, "Tượng", true, 2, 9, R.drawable.red_elephant));
        pieces.add(new Piece(13, "Tượng", true, 6, 9, R.drawable.red_elephant));
        pieces.add(new Piece(14, "Sĩ", true, 3, 9, R.drawable.red_guard));
        pieces.add(new Piece(15, "Sĩ", true, 5, 9, R.drawable.red_guard));
        pieces.add(new Piece(16, "Tướng", true, 4, 9, R.drawable.red_general));
        /**/
        pieces.add(new Piece(17, "Tốt", false, 0, 3, R.drawable.black_soldier));
        pieces.add(new Piece(18, "Tốt", false, 2, 3, R.drawable.black_soldier));
        pieces.add(new Piece(19, "Tốt", false, 4, 3, R.drawable.black_soldier));
        pieces.add(new Piece(20, "Tốt", false, 6, 3, R.drawable.black_soldier));
        pieces.add(new Piece(21, "Tốt", false, 8, 3, R.drawable.black_soldier));
        pieces.add(new Piece(22, "Pháo", false, 1, 2, R.drawable.black_cannon));
        pieces.add(new Piece(23, "Pháo", false, 7, 2, R.drawable.black_cannon));
        pieces.add(new Piece(24, "Xe", false, 0, 0, R.drawable.black_chariot));
        pieces.add(new Piece(25, "Xe", false, 8, 0, R.drawable.black_chariot));
        pieces.add(new Piece(26, "Mã", false, 1, 0, R.drawable.black_horse));
        pieces.add(new Piece(27, "Mã", false, 7, 0, R.drawable.black_horse));
        pieces.add(new Piece(28, "Tượng", false, 2, 0, R.drawable.black_elephant));
        pieces.add(new Piece(29, "Tượng", false, 6, 0, R.drawable.black_elephant));
        pieces.add(new Piece(30, "Sĩ", false, 3, 0, R.drawable.black_guard));
        pieces.add(new Piece(31, "Sĩ", false, 5, 0, R.drawable.black_guard));
        pieces.add(new Piece(32, "Tướng", false, 4, 0, R.drawable.black_general));
        return pieces;
    }

    public static List<Training> trainingData() {
        List<Training> trainings = new ArrayList<>();
        trainings.add(new Training(1L, "Tuyển tập pháo đầu", null));
        trainings.add(new Training(2L, "Tuyển tập bình phong mã", null));
        trainings.add(new Training(3L, "Tuyển tập phi tượng cục", null));
        trainings.add(new Training(4L, "Thuận pháo", 1L));
        trainings.add(new Training(5L, "Nghịch pháo", 1L));
        trainings.add(new Training(6L, "Trận 1", 4L));
        trainings.add(new Training(7L, "Trận 1", 5L));

        return trainings;
    }

    public static Training findTrainingById(Long id) {
        return trainingData().stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static List<Training> findAllChildrenTrainingByParentTrainingId(Long parentTrainingId) {
        return trainingData().stream()
                .filter(t -> Objects.equals(t.getParentTrainingId(), parentTrainingId))
                .collect(Collectors.toList());
    }

    public static List<TrainingDetail> trainingDetailData() {
        List<TrainingDetail> trainingDetails = new ArrayList<>();
        AtomicReference<PlayBoard> currentBoard = new AtomicReference<>(generatePlayBoard());

        /**//**////////////////////////////////////////////////
        TrainingDetail trainingDetail1 = new TrainingDetail();
        trainingDetail1.setTraining(findTrainingById(6L));

        Map<Integer, Move> moves = new HashMap<>();
        moves.put(1, createMove(currentBoard, 7, 4, 7));
        moves.put(2, createMove(currentBoard, 23, 4, 2));
        moves.put(3, createMove(currentBoard, 11, 6, 7));
        moves.put(4, createMove(currentBoard, 27, 6, 2));
        moves.put(5, createMove(currentBoard, 9, 8, 8));
        moves.put(6, createMove(currentBoard, 25, 7, 0));
        moves.put(7, createMove(currentBoard, 9, 3, 8));
        moves.put(8, createMove(currentBoard, 25, 7, 6));
        moves.put(9, createMove(currentBoard, 9, 3, 1));
        moves.put(10, createMove(currentBoard, 26, 0, 2));
        moves.put(11, createMove(currentBoard, 8, 0, 8));
        moves.put(12, createMove(currentBoard, 22, 1, 9));
        moves.put(13, createMove(currentBoard, 6, 1, 2));
        moves.put(14, createMove(currentBoard, 25, 7, 2));
        moves.put(15, createMove(currentBoard, 9, 6, 1));
        moves.put(16, createMove(currentBoard, 24, 1, 0));
        moves.put(17, createMove(currentBoard, 6, 6, 2));
        moves.put(18, createMove(currentBoard, 29, 8, 2));
        moves.put(19, createMove(currentBoard, 7, 4, 3));
        moves.put(20, createMove(currentBoard, 31, 4, 1));
        moves.put(21, createMove(currentBoard, 6, 8, 2));
        moves.put(22, createMove(currentBoard, 25, 7, 0));
        moves.put(23, createMove(currentBoard, 8, 7, 8));
        moves.put(24, createMove(currentBoard, 25, 5, 0));
        moves.put(25, createMove(currentBoard, 9, 7, 1));
        moves.put(26, createMove(currentBoard, 24, 1, 4));
        moves.put(27, createMove(currentBoard, 6, 8, 0));
        moves.put(28, createMove(currentBoard, 25, 8, 0));
        moves.put(29, createMove(currentBoard, 9, 7, 0));
        moves.put(30, createMove(currentBoard, 25, 7, 0));
        moves.put(31, createMove(currentBoard, 8, 7, 0));
        trainingDetail1.setMoves(moves);

        trainingDetail1.setTotalTurn(moves.size());
        currentBoard.set(generatePlayBoard());
        /**/

        trainingDetails.add(trainingDetail1);

        return trainingDetails;
    }

    public static Move createMove(AtomicReference<PlayBoard> currentBoard, int pieceId, int toCol, int toRow) {
        Move move = new Move();

        Piece movingPiece = Support.findPieceInBoard(pieceId, currentBoard.get());
        if (movingPiece == null) {
            System.out.println(pieceId);
            throw new IllegalStateException("Piece with id = " + pieceId + "is dead");
        } else {
            move.setMovingPiece(new Piece(movingPiece));
        }

        currentBoard.set(Support.updatePlayBoard(currentBoard.get(), movingPiece, toCol, toRow));
        move.setUpdatedPlayBoard(currentBoard.get());

        return move;
    }

    public static TrainingDetail findTrainingDetailById(long id) {
        return trainingDetailData().stream()
                .filter(td -> Objects.equals(td.getTraining().getId(), id))
                .findFirst()
                .orElse(new TrainingDetail(findTrainingById(id), 0,null));
    }

    public static PlayBoard generatePlayBoard() {
        PlayBoard playBoard = new PlayBoard();

        pieceData().forEach(piece -> playBoard.getState()[piece.getCurrentCol()][piece.getCurrentRow()] = new Piece(piece));

        return playBoard;
    }

}
