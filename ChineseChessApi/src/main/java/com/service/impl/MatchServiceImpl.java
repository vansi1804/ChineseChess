package com.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.data.dto.MatchCreationDTO;
import com.data.dto.MatchDTO;
import com.data.dto.MatchDetailDTO;
import com.data.dto.MatchStartDTO;
import com.data.dto.MoveHistoryDTO;
import com.data.entity.Match;
import com.data.mapper.MatchMapper;
import com.data.repository.MatchRepository;
import com.data.repository.PlayerRepository;
import com.exception.EndMatchException;
import com.exception.ResourceNotFoundException;
import com.service.MatchService;
import com.service.MoveHistoryService;
import com.service.PlayBoardService;

@Service
public class MatchServiceImpl implements MatchService {
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private MatchMapper matchMapper;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayBoardService playBoardService;
    @Autowired
    private MoveHistoryService moveHistoryService;

    @Override
    public List<MatchDTO> findAll() {
        return matchRepository.findAll().stream().map(m -> matchMapper.toDTO(m)).collect(Collectors.toList());
    }

    @Override
    public MatchDTO findById(long id) {
        return matchRepository.findById(id)
                .map(m -> matchMapper.toDTO(m))
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id)));
    }

    @Override
    public List<MatchDTO> findAllByPlayerId(long playerId) {
        return matchRepository.findAllByPlayerId(playerId).stream()
                .map(m -> matchMapper.toDTO(m))
                .collect(Collectors.toList());
    }

    @Override
    public MatchDetailDTO findMatchDetailById(long id) {
        MatchDetailDTO matchDetailDTO = matchRepository.findById(id)
                .map(m -> matchMapper.toDetailDTO(m))
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        List<MoveHistoryDTO> moveHistoryDTOs = moveHistoryService
                .findAllByMatchId(matchDetailDTO.getMatchDTO().getId());

        for (MoveHistoryDTO moveHistoryDTO : moveHistoryDTOs) {
            System.out.println("\n" + moveHistoryDTO.getTurn() + ":\t" + moveHistoryDTO.getDescription());
            moveHistoryDTO.getCurrentBoard().print(moveHistoryDTO.getMovingPieceDTO());
        }

        matchDetailDTO.setTotalTurn((long) moveHistoryDTOs.size());
        matchDetailDTO.setMoveHistoryDTOs(moveHistoryDTOs);
        return matchDetailDTO;
    }

    @Override
    public MatchStartDTO create(MatchCreationDTO matchCreationDTO) {
        Map<String, Object> errors = new HashMap<String, Object>();
        if (!playerRepository.existsById(matchCreationDTO.getPlayer1Id())) {
            errors.put("player1Id", matchCreationDTO.getPlayer1Id());
        }
        if (!playerRepository.existsById(matchCreationDTO.getPlayer2Id())) {
            errors.put("player2Id", matchCreationDTO.getPlayer2Id());
        }
        if (!errors.isEmpty()) {
            throw new ResourceNotFoundException(errors);
        }

        Match match = matchRepository.saveAndFlush(matchMapper.toEntity(matchCreationDTO));

        MatchStartDTO matchStartDTO = matchMapper.toStartDTO(match);
        matchStartDTO.setDeadPieceDTOs(new ArrayList<>());
        matchStartDTO.setPlayBoardStartDTO(playBoardService.create());
        return matchStartDTO;
    }

    @Override
    public MatchDTO updateResult(long id, Boolean isRedWin) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        if (match.getResult() != null) {
            throw new EndMatchException(Collections.singletonMap("id", id));
        }

        long winnerId = (isRedWin == null)
                ? 0
                : isRedWin
                        ? match.getPlayer1().getId()
                        : match.getPlayer2().getId();
        match.setResult(winnerId);

        return matchMapper.toDTO(matchRepository.save(match));
    }

}
