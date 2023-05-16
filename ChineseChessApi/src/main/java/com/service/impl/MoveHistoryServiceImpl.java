package com.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.dto.GameViewDTO;
import com.data.dto.MoveHistoryCreationDTO;
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
    public List<GameViewDTO> findAllByMatchId(long matchId) {
        PlayBoardDTO currentBoard = playBoardService.create();
        List<PieceDTO> deadPieceDTOs = new ArrayList<>();
        return moveHistoryRepository.findAllByMatchId(matchId).stream()
                .map(mh -> {
                    GameViewDTO gameViewDTO = moveHistoryMapper.toDTO(mh);
                    Piece deadPieceInThisTurn = findLastedMoveUtilTurnByMatchIdAndColAndRowMovingTo(
                            mh.getMatch().getId(), mh.getTurn() - 1, mh.getToCol(), mh.getToRow());
                    if (deadPieceInThisTurn != null) {
                        deadPieceDTOs.add(pieceMapper.toDTO(deadPieceInThisTurn));
                    }
                    gameViewDTO.setDeadPieceDTOs(deadPieceDTOs);
                    gameViewDTO.setDescription(moveDescriptionService.getDescription(currentBoard, mh));
                    gameViewDTO.setPlayBoard(playBoardService.update(currentBoard, mh));

                    return gameViewDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PieceDTO> create(MoveHistoryCreationDTO moveHistoryCreationDTO) {
        if (!matchRepository.existsById(moveHistoryCreationDTO.getMatchId())) {
            Map<String, Object> errors = new HashMap<String, Object>();
            errors.put("matchId", moveHistoryCreationDTO.getMatchId());
            throw new ResourceNotFoundException(errors);
        }
        MoveHistory moveHistory = moveHistoryMapper.toEntity(moveHistoryCreationDTO);
        moveHistory.setMatch(matchRepository.findById(moveHistoryCreationDTO.getMatchId()).get());
        moveHistory.setTurn(moveHistoryRepository.countTurnByMatchId(moveHistoryCreationDTO.getMatchId()) + 1);
        moveHistory.setPiece(pieceRepository.findById(moveHistoryCreationDTO.getPieceId()).get());
        moveHistoryRepository.save(moveHistory);

        Piece deadPieceInThisTurn = findLastedMoveUtilTurnByMatchIdAndColAndRowMovingTo(
                moveHistoryCreationDTO.getMatchId(), moveHistory.getTurn() - 1,
                moveHistoryCreationDTO.getToCol(), moveHistoryCreationDTO.getToRow());
        if (deadPieceInThisTurn != null) {
            moveHistoryCreationDTO.getDeadPieceDTOs().add(pieceMapper.toDTO(deadPieceInThisTurn));
        }
        return moveHistoryCreationDTO.getDeadPieceDTOs();
    }

    private Piece findLastedMoveUtilTurnByMatchIdAndColAndRowMovingTo(long matchId, long turn, int colMovingTo,
            int rowMovingTo) {
        Optional<MoveHistory> lastedMoveToColAndRow = moveHistoryRepository
                .findLastedMoveUtilTurnByMatchIdAndColAndRowMovingTo(matchId, turn, colMovingTo, rowMovingTo);
        return lastedMoveToColAndRow.isPresent() ? lastedMoveToColAndRow.get().getPiece() : null;
    }

}
