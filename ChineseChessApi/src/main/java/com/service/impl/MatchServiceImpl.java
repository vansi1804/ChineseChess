package com.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.ErrorMessage;
import com.data.dto.MatchCreationDTO;
import com.data.dto.MatchDTO;
import com.data.dto.MatchDetailDTO;
import com.data.dto.MoveHistoryDTO;
import com.data.entity.Match;
import com.data.entity.MoveHistory;
import com.data.mapper.MatchMapper;
import com.data.repository.MatchRepository;
import com.data.repository.MoveHistoryRepository;
import com.data.repository.PlayerRepository;
import com.config.exception.InvalidException;
import com.config.exception.ResourceNotFoundException;
import com.service.MatchService;
import com.service.MoveHistoryService;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final PlayerRepository playerRepository;
    private final MoveHistoryRepository moveHistoryRepository;
    private final MoveHistoryService moveHistoryService;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository,
            MatchMapper matchMapper,
            PlayerRepository playerRepository,
            MoveHistoryRepository moveHistoryRepository,
            MoveHistoryService moveHistoryService) {
        this.matchRepository = matchRepository;
        this.matchMapper = matchMapper;
        this.playerRepository = playerRepository;
        this.moveHistoryRepository = moveHistoryRepository;
        this.moveHistoryService = moveHistoryService;
    }

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
    public MatchDetailDTO findDetailById(long id) {
        MatchDetailDTO matchDetailDTO = matchRepository.findById(id)
                .map(m -> matchMapper.toDetailDTO(m))
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        List<MoveHistory> moveHistories = moveHistoryRepository.findAllByMatch_Id(id);
        Map<Long, MoveHistoryDTO> moveHistoryDTOs = moveHistoryService.build(moveHistories);

        matchDetailDTO.setTotalTurn((long) moveHistoryDTOs.size());
        matchDetailDTO.setMoveHistoryDTOs(moveHistoryDTOs);
        return matchDetailDTO;
    }

    @Override
    public MatchDTO create(MatchCreationDTO matchCreationDTO) {
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

        if (matchRepository.existsPlayingByPlayerId(matchCreationDTO.getPlayer1Id())) {
            errors.put("player1Id", matchCreationDTO.getPlayer1Id());
        }

        if (matchRepository.existsPlayingByPlayerId(matchCreationDTO.getPlayer2Id())) {
            errors.put("player2Id", matchCreationDTO.getPlayer2Id());
        }

        if (!errors.isEmpty()) {
            throw new InvalidException(ErrorMessage.PLAYER_PLAYING, errors);
        }

        return matchMapper.toDTO(matchRepository.save(matchMapper.toEntity(matchCreationDTO)));
    }

    @Override
    public MatchDTO updateResult(long id, Boolean isRedWin) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Collections.singletonMap("id", id)));

        if (match.getResult() != null) {
            throw new InvalidException(ErrorMessage.END_MATCH, Collections.singletonMap("id", match.getId()));
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
