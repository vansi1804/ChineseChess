package com.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.dto.GameViewDTO;
import com.data.dto.MoveHistoryDTO;
import com.data.dto.PieceDTO;
import com.data.entity.MoveHistory;
import com.data.entity.Piece;
import com.data.mapper.MoveHistoryMapper;
import com.data.mapper.PieceMapper;
import com.data.repository.MatchRepository;
import com.data.repository.MoveHistoryRepository;
import com.data.repository.PieceRepository;
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
    public List<GameViewDTO> findAllByMatchId(long matchId) {
        Piece[][] currentBoard = pieceMapper.toEntity(playBoardService.create());
        List<PieceDTO> deadPieceDTOs = new ArrayList<>();
        return moveHistoryRepository.findAllByMatchId(matchId).stream()
                .map(mh -> {
                    GameViewDTO gameViewDTO = moveHistoryMapper.toDTO(mh);
                    gameViewDTO.setBoard(playBoardService.update(currentBoard, mh));
                    Piece deadPieceInThisTurn = this.findLastedPieceStandingColAndRowMovingInTurn(
                            mh.getMatch().getId(), mh.getTurn() - 1, mh.getToCol(), mh.getToRow());
                    if (deadPieceInThisTurn != null) {
                        deadPieceDTOs.add(pieceMapper.toDTO(deadPieceInThisTurn));
                    }
                    gameViewDTO.setDeadPieceDTOs(deadPieceDTOs);
                    gameViewDTO.setDescription(moveDescriptionService.getDescription(currentBoard, mh));
                    return gameViewDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PieceDTO> create(MoveHistoryDTO moveHistoryDTO) {
        MoveHistory moveHistory = moveHistoryMapper.toEntity(moveHistoryDTO);
        moveHistory.setMatch(matchRepository.findById(moveHistoryDTO.getMatchId()).get());
        moveHistory.setTurn(moveHistoryRepository.count() + 1);
        moveHistory.setPiece(pieceRepository.findById(moveHistoryDTO.getPieceId()).get());
        moveHistoryRepository.save(moveHistory);

        Piece deadPieceInThisTurn = this.findLastedPieceStandingColAndRowMovingInTurn(
                moveHistoryDTO.getMatchId(), moveHistory.getTurn() - 1,
                moveHistoryDTO.getToCol(), moveHistoryDTO.getToRow());
        if (deadPieceInThisTurn != null) {
            moveHistoryDTO.getDeadPieceDTOs().add(pieceMapper.toDTO(deadPieceInThisTurn));
        }
        return moveHistoryDTO.getDeadPieceDTOs();
    }

    private Piece findLastedPieceStandingColAndRowMovingInTurn(long matchId, long turn, int colMovingTo,
            int rowMovingTo) {
        Optional<MoveHistory> lastedMoveToColAndRow = moveHistoryRepository
                .findLastedMoveUtilTurnByMatchIdAndColAndRowMovingTo(matchId, turn, colMovingTo, rowMovingTo);
        return lastedMoveToColAndRow.isPresent() ? lastedMoveToColAndRow.get().getPiece() : null;
    }

}
