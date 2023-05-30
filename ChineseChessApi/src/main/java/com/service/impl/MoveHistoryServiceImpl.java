package com.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.dto.MoveHistoryCreationDTO;
import com.data.dto.MoveHistoryDTO;
import com.data.dto.PieceDTO;
import com.data.dto.PlayBoardDTO;
import com.data.entity.MoveHistory;
import com.data.entity.Piece;
import com.data.mapper.MoveHistoryMapper;
import com.data.mapper.PieceMapper;
import com.data.repository.MatchRepository;
import com.data.repository.MoveHistoryRepository;
import com.data.repository.PieceRepository;
import com.exception.ResourceNotFoundException;
import com.service.MoveHistoryService;
import com.service.MoveDescriptionService;
import com.service.PlayBoardService;

@Service
public class MoveHistoryServiceImpl implements MoveHistoryService {
    @Autowired
    private MoveHistoryRepository moveHistoryRepository;
    @Autowired
    private MoveHistoryMapper moveHistoryMapper;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private PlayBoardService playBoardService;
    @Autowired
    private MoveDescriptionService moveDescriptionService;
    @Autowired
    private PieceRepository pieceRepository;
    @Autowired
    private PieceMapper pieceMapper;

    @Override
    public List<MoveHistoryDTO> findAllByMatchId(long matchId) {
        PlayBoardDTO currentBoard = playBoardService.create();
        List<PieceDTO> deadPieceDTOs = new ArrayList<>();
        return moveHistoryRepository.findAllByMatch_Id(matchId).stream()
                .map(mh -> {
                    MoveHistoryDTO moveHistoryDTO = new MoveHistoryDTO();
                    moveHistoryDTO.setTurn(mh.getTurn());
                    Piece deadPieceInThisTurn = findPieceInColAndRowMovingTo(
                            mh.getMatch().getId(), mh.getTurn() - 1, mh.getToCol(), mh.getToRow());
                    if (deadPieceInThisTurn != null) {
                        deadPieceDTOs.add(pieceMapper.toDTO(deadPieceInThisTurn));
                    }
                    moveHistoryDTO.setDeadPieceDTOs(deadPieceDTOs);
                    moveHistoryDTO.setDescription(moveDescriptionService.getDescription(currentBoard, mh));
                    moveHistoryDTO.setPlayBoard(playBoardService.update(currentBoard, mh));
                    return moveHistoryDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PieceDTO> create(MoveHistoryCreationDTO moveHistoryCreationDTO) {
        if (!matchRepository.existsById(moveHistoryCreationDTO.getMatchId())) {
            throw new ResourceNotFoundException(
                    Collections.singletonMap("matchId", moveHistoryCreationDTO.getMatchId()));
        }
        MoveHistory moveHistory = moveHistoryMapper.toEntity(moveHistoryCreationDTO);
        moveHistory.setTurn(moveHistoryRepository.countTurnByMatch_Id(moveHistoryCreationDTO.getMatchId()) + 1);
        moveHistoryRepository.save(moveHistory);

        Piece deadPieceInThisTurn = findPieceInColAndRowMovingTo(
                moveHistoryCreationDTO.getMatchId(), moveHistory.getTurn() - 1,
                moveHistoryCreationDTO.getToCol(), moveHistoryCreationDTO.getToRow());
        if (deadPieceInThisTurn != null) {
            moveHistoryCreationDTO.getDeadPieceDTOs().add(pieceMapper.toDTO(deadPieceInThisTurn));
        }
        return moveHistoryCreationDTO.getDeadPieceDTOs();
    }

    private Piece findPieceInColAndRowMovingTo(long matchId, long turn, int colMovingTo, int rowMovingTo) {
        Piece deadPiece = null;

        Optional<MoveHistory> lastedMoveToColAndRow = moveHistoryRepository
                .findLastedMoveUtilTurnByMatchIdAndColAndRowMovingTo(matchId, turn, colMovingTo, rowMovingTo);

        if (lastedMoveToColAndRow.isPresent()) {
            deadPiece = lastedMoveToColAndRow.get().getPiece();
        } else {
            Optional<Piece> startPiece = pieceRepository.findByCurrentColAndCurrentRow(colMovingTo, rowMovingTo);
            deadPiece = startPiece.isPresent() ? startPiece.get() : null;
        }

        return deadPiece;
    }

}
